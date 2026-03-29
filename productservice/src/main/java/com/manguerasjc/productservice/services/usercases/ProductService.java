package com.manguerasjc.productservice.services.usercases;

import com.manguerasjc.productservice.controllers.ProductController;
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
import openize.heic.decoder.HeicImage;
import openize.heic.decoder.PixelFormat;
import openize.io.IOFileStream;
import openize.io.IOMode;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.logging.Logger;
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

        String originalName = image.getOriginalFilename();
        if (originalName == null || originalName.isBlank()) {
            throw new IllegalArgumentException("El archivo no tiene nombre");
        }

        boolean heic = originalName.toLowerCase().matches(".*\\.(heic|heif)$");

        String finalFileName = System.currentTimeMillis() + "_" +
                (heic ? originalName.replaceAll("(?i)\\.(heic|heif)$", ".jpg")
                        : originalName);

        Path uploadDir = Paths.get("uploads");
        if (!Files.exists(uploadDir)) Files.createDirectories(uploadDir);

        Path outputPath = uploadDir.resolve(finalFileName);

        System.out.println("Procesando imagen: " + originalName + " heic=" + heic + " destino=" + outputPath);

        if (heic) {
            Path temp = Files.createTempFile("heic_", ".heic");
            System.out.println("Temporal creado en: " + temp);
            try {
                image.transferTo(temp.toFile());
                System.out.println("Archivo transferido, tamaño: " + Files.size(temp) + " bytes");

                try (IOFileStream fs = new IOFileStream(temp.toString(), IOMode.READ)) {
                    HeicImage heicImage = HeicImage.load(fs);
                    System.out.println("HEIC cargado, dimensiones: " + heicImage.getWidth() + "x" + heicImage.getHeight());

                    var frames = heicImage.getFrames();
                    System.out.println("Frames encontrados: " + (frames == null ? "null" : frames.size()));

                    if (frames == null || frames.isEmpty()) {
                        throw new IOException("El archivo HEIC no contiene frames válidos");
                    }

                    int width  = (int) heicImage.getWidth();
                    int height = (int) heicImage.getHeight();
                    if (width <= 0 || height <= 0) {
                        throw new IOException("Dimensiones inválidas: " + width + "x" + height);
                    }

                    BufferedImage buffered = null;

                    for (var entry : frames.entrySet()) {
                        try {
                            var frame = entry.getValue();
                            if (frame == null) {
                                System.out.println("Frame " + entry.getKey() + " es null, saltando");
                                continue;
                            }

                            int[] pixels = frame.getInt32Array(PixelFormat.Argb32);
                            if (pixels == null || pixels.length != width * height) {
                                System.out.println("Frame " + entry.getKey() + " tiene píxeles inválidos, saltando");
                                continue;
                            }

                            buffered = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
                            buffered.setRGB(0, 0, width, height, pixels, 0, width);
                            System.out.println("Frame válido encontrado: " + entry.getKey());
                            break;

                        } catch (Exception e) {
                            System.out.println("Frame " + entry.getKey() + " falló (" + e.getMessage() + "), saltando");
                        }
                    }

                    if (buffered == null) {
                        throw new IOException("No se encontró ningún frame válido en el HEIC");
                    }

                    Thumbnails.of(buffered)
                            .size(600, 600)
                            .outputFormat("jpg")
                            .toFile(outputPath.toFile());
                    System.out.println("Thumbnail guardado en: " + outputPath);

                } catch (IOException e) {
                    throw e;
                } catch (Exception e) {
                    System.out.println("Fallo dentro del bloque HEIC: " + e);
                    throw new IOException("Error procesando HEIC: " + e.getMessage(), e);
                }

            } finally {
                Files.deleteIfExists(temp);
                System.out.println("Temporal eliminado");
            }

        } else {
            try {
                Thumbnails.of(image.getInputStream())
                        .size(600, 600)
                        .toFile(outputPath.toFile());
                System.out.println("Imagen normal guardada en: " + outputPath);
            } catch (IOException e) {
                System.out.println("Fallo procesando imagen normal: " + e);
                throw new IOException("Error procesando la imagen: " + e.getMessage(), e);
            }
        }

        return "/uploads/" + finalFileName;
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

}
