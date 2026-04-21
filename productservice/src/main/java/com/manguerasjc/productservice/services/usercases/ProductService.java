package com.manguerasjc.productservice.services.usercases;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.manguerasjc.productservice.dataAccess.domain.Product;
import com.manguerasjc.productservice.dataAccess.domain.ProductVariant;
import com.manguerasjc.productservice.dataAccess.domain.Size;
import com.manguerasjc.productservice.dataAccess.repositories.*;
import com.manguerasjc.productservice.services.DTO.mapper.ProductMapper;
import com.manguerasjc.productservice.services.DTO.request.ProductRequestDTO;
import com.manguerasjc.productservice.services.DTO.response.ProductResponseDTO;
import com.manguerasjc.productservice.services.exceptions.StockNoValidoException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProductService implements IProductService{
    @Autowired
    IProductRepository productRepository;
    @Autowired
    ICategoryRepository categoryRepository;
    @Autowired
    IBrandRepository brandRepository;
    @Autowired
    ISizeRepository sizeRepository;
    @Autowired
    IColorRepository colorRepository;
    @Autowired
    ProductMapper productMapper;
    @Autowired
    private Cloudinary cloudinary;


    public String imageToUrl(MultipartFile image) throws IOException {

        String originalName = image.getOriginalFilename();
        if (originalName == null || originalName.isBlank()) {
            throw new IllegalArgumentException("El archivo no tiene nombre");
        }

        if (image.getSize() > 10 * 1024 * 1024) {
            throw new IllegalArgumentException("La imagen no puede superar 10MB");
        }

        Map uploadResult = cloudinary.uploader().upload(image.getBytes(), ObjectUtils.asMap(
                "folder", "products",
                "resource_type", "image",
                "transformation", Arrays.asList(
                        ObjectUtils.asMap(
                                "width", 600,
                                "height", 600,
                                "crop", "limit",
                                "quality", "80",
                                "fetch_format", "auto"
                        )
                )
        ));

        return (String) uploadResult.get("secure_url");
    }
    public void deleteImage(String imageUrl) {
        if (imageUrl != null && !imageUrl.isEmpty()) {
            try {
                // imageUrl = "/uploads/12345_nombre.png"
                String publicId = imageUrl
                        .replaceAll("https://res.cloudinary.com/[^/]+/image/upload/v\\d+/", "")
                        .replaceAll("\\.[^.]+$", "");
                cloudinary.uploader().destroy(publicId, ObjectUtils.asMap("resource_type", "image"));
            } catch (Exception e) {
                throw new RuntimeException("Error al eliminar la imagen en Cloudinary: " + imageUrl, e);
            }
        }
    }

    public List<ProductVariant>mapVariants(Product productEntity,Map<Long,Integer> variantesDTO){
        // Mapear el mismo producto de diferente talla
        List<ProductVariant> productVariants = new ArrayList<>();
        // Recorrer el diccionario que almacena los productos y guardarlos
        for(Map.Entry<Long, Integer> entry : variantesDTO.entrySet()){
            // entry.getValue() = stock
            // entry.getKey()= size
            // Si el stock ingresado es mayor a 0
            if(entry.getValue()>0){
                Size size =sizeRepository.findById(entry.getKey()).orElseThrow(() -> new EntityNotFoundException("Talla no encontrada"));
                ProductVariant productVariant = new ProductVariant();
                productVariant.setSize(size);
                productVariant.setStock(entry.getValue());
                productVariants.add(productVariant);
                productVariant.setProduct(productEntity);
            }
        }

        if(productVariants.isEmpty()){
            throw new StockNoValidoException("Al menos un producto debe tener un stock superior a 0");
        }
        return productVariants;
    }


    @Transactional
    @Override
    public ProductResponseDTO addProduct(ProductRequestDTO productRequestDTO) {
        Product product = productMapper.toEntity(productRequestDTO);

        System.out.println("llego aqui, el color es ");
        // Validar si el color existe
        product.setColor(
                colorRepository.findById(productRequestDTO.colorId()).
                        orElseThrow(() -> new EntityNotFoundException("Color no encontrado"))
        );
        System.out.println("llego aqui, el color es "+product.getColor().getName());

        // Validar si categoría existe
        product.setCategory(
                categoryRepository.findById(productRequestDTO.categoryId()).
                        orElseThrow(() -> new EntityNotFoundException("Categoria no encontrada"))
        );

        product.setVariants(mapVariants(product, productRequestDTO.productVariantRequestDTO().getVariants()));

        // Validar si la marca existe
        product.setBrand(
                brandRepository.findById(productRequestDTO.brandId()).
                        orElseThrow(() -> new EntityNotFoundException("Marca no encontrada"))
        );


        // Recibir imágen
        try{
            // Revisar si la imagen no esta vacia
            if(productRequestDTO.images()!=null && !productRequestDTO.images().isEmpty()){
                for(MultipartFile image: productRequestDTO.images()){
                    System.out.println("Image: "+image);
                    product.getUrlImages().add(imageToUrl(image));
                }
            }
        }catch (IOException e){
            throw new RuntimeException("Error al guardar la imagen",e);
        }
        product = productRepository.save(product);
        product.setName(product.getBrand().getBrand()+" "+product.getColor().getName()+" "+product.getId());

        return productMapper.toResponseDTO(productRepository.save(product));
    }
    @Transactional
    @Override
    public ProductResponseDTO updateProduct(Long id, ProductRequestDTO productRequestDTO) {


        // Validar si existe
        Product productEntity = productRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("Producto no encontrado"));

        // Validar si el color existe
        productEntity.setColor(
                colorRepository.findById(productRequestDTO.colorId()).
                        orElseThrow(() -> new EntityNotFoundException("Color no encontrado"))
        );

        productEntity.setPrice(productRequestDTO.price());

        // Validar si categoría existe
        productEntity.setCategory(
                categoryRepository.findById(productRequestDTO.categoryId()).
                        orElseThrow(() -> new EntityNotFoundException("Categoria no encontrada"))
        );

        productEntity.getVariants().clear();
        productEntity.getVariants().addAll(mapVariants(productEntity,productRequestDTO.productVariantRequestDTO().getVariants()));

        // Validar si la marca existe
        productEntity.setBrand(
                brandRepository.findById(productRequestDTO.brandId()).
                        orElseThrow(() -> new EntityNotFoundException("Marca no encontrada"))
        );


        // Recibir imágen

        try{
// Borrar solo las imágenes que ya no están en existingImages
            List<String> existing = productRequestDTO.existingImages() != null ? productRequestDTO.existingImages() : List.of();

            for (String url : productEntity.getUrlImages()) {
                if (!existing.contains(url)) {
                    deleteImage(url); // solo borra las que el usuario quitó
                }
            }

// Empezar con las existentes que se conservan
            List<String> urlImagenes = new ArrayList<>(existing);

// Agregar las nuevas
            if (productRequestDTO.images() != null && !productRequestDTO.images().isEmpty()) {
                for (MultipartFile image : productRequestDTO.images()) {
                    urlImagenes.add(imageToUrl(image));
                }
            }
            productEntity.setUrlImages(urlImagenes);
        }catch (IOException e){
            throw new RuntimeException("Error al guardar la imagen",e);
        }
        productEntity = productRepository.save(productEntity);
        productEntity.setName(productEntity.getBrand().getBrand()+" "+productEntity.getColor().getName()+" "+productEntity.getId());

        return productMapper.toResponseDTO(productEntity);

    }

    @Override
    public void deleteProduct(Long id) {
        Product productEntity = productRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Producto no encontrado"));
        for(String image:productEntity.getUrlImages()){
            deleteImage(image);
        }
        productRepository.deleteById(id);
    }

    @Override
    public List<ProductResponseDTO> getProducts() {
        return productRepository.findAll().stream().
                map(p -> productMapper.toResponseDTO(p)).collect(Collectors.toList());
    }

    @Override
    public List<ProductResponseDTO>getProductsByCategoryId(Long categoryId){
        List<Product> productsFilterByCategory = productRepository.findProductsByCategory_Id(categoryId);
        return productsFilterByCategory.stream().map(
                product -> productMapper.toResponseDTO(product)).collect(Collectors.toList());
    }
    @Override
    public List<ProductResponseDTO>findProductsWithFilters(List<Long> categoriesIds, List<Long> brandsIds, List<Long> sizesIds){
        Specification<Product>spec = Specification.allOf(
                        ProductSpecification.hasCategoriesIds(categoriesIds),
                        ProductSpecification.hasBrandsIds(brandsIds),
                        ProductSpecification.hasSizesIds(sizesIds));

        return productRepository.findAll(spec).stream().map(product -> productMapper.toResponseDTO(product)).collect(Collectors.toList());
    }

    @Override
    public ProductResponseDTO findById(Long productId) {
        Product product = productRepository.findById(productId).orElseThrow(()->new EntityNotFoundException("No se encontro el producto."));
        return productMapper.toResponseDTO(product);
    }

}
