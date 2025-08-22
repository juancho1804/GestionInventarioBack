package com.manguerasjc.productservice.services.usercases;

import com.manguerasjc.productservice.dataAccess.domain.Category;
import com.manguerasjc.productservice.dataAccess.domain.Product;
import com.manguerasjc.productservice.dataAccess.domain.ProductVariant;
import com.manguerasjc.productservice.dataAccess.domain.Size;
import com.manguerasjc.productservice.dataAccess.repositories.*;
import com.manguerasjc.productservice.services.DTO.mapper.ProductMapper;
import com.manguerasjc.productservice.services.DTO.request.ProductRequestDTO;
import com.manguerasjc.productservice.services.DTO.response.ProductResponseDTO;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
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
        Files.copy(image.getInputStream(), path);

        // Devuelvo la URL accesible por el frontend
        return "/uploads/" + fileName;
    }

    @Transactional
    @Override
    public ProductResponseDTO addProduct(ProductRequestDTO productRequestDTO) {

        Product product = productMapper.toEntity(productRequestDTO);
        try{

            if(productRequestDTO.image()!=null && !productRequestDTO.image().isEmpty()){
                product.setUrlImage(imageToUrl(productRequestDTO.image()));
            }
        }catch (IOException e){
            throw new RuntimeException("Error al guardar la imagen",e);
        }

        // Validar si categoría existe
        product.setCategory(
                categoryRepository.findById(productRequestDTO.categoryId()).
                        orElseThrow(() -> new EntityNotFoundException("Categoria no encontrada"))
        );

        Map<Long,Integer> variantesDTO = productRequestDTO.productVariantRequestDTO().getVariantes();
        List<ProductVariant> productVariants = new ArrayList<>();


        for(Map.Entry<Long, Integer> entry : variantesDTO.entrySet()){

            Size size =sizeRepository.findById(entry.getKey()).orElseThrow(() -> new EntityNotFoundException("Talla no encontrada"));
            ProductVariant productVariant = new ProductVariant();
            productVariant.setSize(size);
            productVariant.setStock(entry.getValue());
            productVariants.add(productVariant);
            System.out.println("Talla: " + entry.getKey()+", Stock: " + entry.getValue());
            productVariant.setProduct(product);
        }

        product.setVariants(productVariants);


        // Validar si la marca existe
        product.setBrand(
                brandRepository.findById(productRequestDTO.brandId()).
                        orElseThrow(() -> new EntityNotFoundException("Marca no encontrada"))
        );

        Long conteo = productRepository.count();

        product.setName(product.getBrand().getBrand()+product.getColor()+conteo);

        return productMapper.toResponseDTO(productRepository.save(product));
    }

    @Override
    public ProductResponseDTO updateProduct(Long id, ProductRequestDTO productRequestDTO) {


        // Validar si existe
        Product productEntity = productRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("Producto no encontrado"));


        productEntity = productMapper.toEntity(productRequestDTO);
        productEntity.setId(id);

        // Validar si categoría existe
        productEntity.setCategory(
                categoryRepository.findById(productRequestDTO.categoryId()).
                        orElseThrow(() -> new EntityNotFoundException("Categoria no encontrada"))
        );

        // Validar si la marca existe
        productEntity.setBrand(
                brandRepository.findById(productRequestDTO.brandId()).
                        orElseThrow(() -> new EntityNotFoundException("Marca no encontrada"))
        );


        productEntity.setName(productEntity.getBrand().getBrand()+productEntity.getColor()+productEntity.getId());

        return productMapper.toResponseDTO(productRepository.save(productEntity));

    }

    @Override
    public void deleteProduct(Long id) {
        if(!productRepository.existsById(id)){
            throw new EntityNotFoundException("El producto a eliminar no existe");
        }
        productRepository.deleteById(id);

    }

    @Override
    public List<ProductResponseDTO> getProducts() {
        return productRepository.findAll().stream().
                map(p -> productMapper.toResponseDTO(p)).collect(Collectors.toList());
    }
}
