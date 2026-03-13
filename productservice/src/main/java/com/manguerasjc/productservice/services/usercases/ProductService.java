package com.manguerasjc.productservice.services.usercases;

import com.manguerasjc.productservice.dataAccess.domain.Category;
import com.manguerasjc.productservice.dataAccess.domain.Product;
import com.manguerasjc.productservice.dataAccess.domain.ProductVariant;
import com.manguerasjc.productservice.dataAccess.domain.Size;
import com.manguerasjc.productservice.dataAccess.repositories.*;
import com.manguerasjc.productservice.services.DTO.mapper.ProductMapper;
import com.manguerasjc.productservice.services.DTO.request.ProductRequestDTO;
import com.manguerasjc.productservice.services.DTO.response.ProductResponseDTO;
import com.manguerasjc.productservice.services.exceptions.StockNoValidoException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.transaction.Transactional;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
    ProductMapper productMapper;

    public String imageToUrl(MultipartFile image) throws IOException {
        // Nombre único para evitar choques
        String fileName = System.currentTimeMillis() + "_" + image.getOriginalFilename();

        // Ruta relativa (carpeta en la raíz del proyecto)
        Path uploadDir = Paths.get("uploads");
        if (!Files.exists(uploadDir)) {
            Files.createDirectories(uploadDir); // crea la carpeta si no existe
        }

        Path path = uploadDir.resolve(fileName);

        // Usar Thumbnailator para redimensionar antes de guardar
        Thumbnails.of(image.getInputStream())
                .size(600, 600) // máximo ancho y alto
                .toFile(path.toFile());

        // Devuelvo la URL accesible por el frontend
        return "/uploads/" + fileName;
    }

    public void deleteImage(String imageUrl) {
        if (imageUrl != null && !imageUrl.isEmpty()) {
            try {
                // imageUrl = "/uploads/12345_nombre.png"
                String fileName = Paths.get(imageUrl).getFileName().toString();
                Path uploadDir = Paths.get("uploads"); // misma carpeta donde las guardas
                Path filePath = uploadDir.resolve(fileName);

                Files.deleteIfExists(filePath);
            } catch (IOException e) {
                throw new RuntimeException("Error al eliminar la imagen: " + imageUrl, e);
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
        System.out.println("aqui se cae");

        // Validar si categoría existe
        product.setCategory(
                categoryRepository.findById(productRequestDTO.categoryId()).
                        orElseThrow(() -> new EntityNotFoundException("Categoria no encontrada"))
        );

        product.setVariants(mapVariants(product, productRequestDTO.productVariantRequestDTO().getVariants()));

        System.out.println("aqui se cae");
        // Validar si la marca existe
        product.setBrand(
                brandRepository.findById(productRequestDTO.brandId()).
                        orElseThrow(() -> new EntityNotFoundException("Marca no encontrada"))
        );
        System.out.println("aqui se cae");


        // Recibir imágen
        try{
            // Revisar si la imagen no esta vacia
            if(productRequestDTO.image()!=null && !productRequestDTO.image().isEmpty()){

                product.setUrlImage(imageToUrl(productRequestDTO.image()));
            }
        }catch (IOException e){
            throw new RuntimeException("Error al guardar la imagen",e);
        }
        product = productRepository.save(product);
        product.setName(product.getBrand().getBrand()+" "+product.getColor()+" "+product.getId());

        return productMapper.toResponseDTO(productRepository.save(product));
    }
    @Transactional
    @Override
    public ProductResponseDTO updateProduct(Long id, ProductRequestDTO productRequestDTO) {


        // Validar si existe
        Product productEntity = productRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("Producto no encontrado"));


        productEntity.setColor(productRequestDTO.color());
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
            // Revisar si la imagen no esta vacia
            if(productRequestDTO.image()!=null && !productRequestDTO.image().isEmpty()){

                productEntity.setUrlImage(imageToUrl(productRequestDTO.image()));
            }
        }catch (IOException e){
            throw new RuntimeException("Error al guardar la imagen",e);
        }
        productEntity = productRepository.save(productEntity);
        productEntity.setName(productEntity.getBrand().getBrand()+" "+productEntity.getColor()+" "+productEntity.getId());

        return productMapper.toResponseDTO(productEntity);

    }

    @Override
    public void deleteProduct(Long id) {
        Product productEntity = productRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Producto no encontrado"));
        deleteImage(productEntity.getUrlImage());
        productRepository.deleteById(id);
    }

    @Override
    public List<ProductResponseDTO> getProducts() {
        return productRepository.findAll().stream().
                map(p -> productMapper.toResponseDTO(p)).collect(Collectors.toList());
    }
}
