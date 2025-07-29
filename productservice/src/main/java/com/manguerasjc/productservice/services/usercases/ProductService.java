package com.manguerasjc.productservice.services.usercases;

import com.manguerasjc.productservice.dataAccess.domain.Category;
import com.manguerasjc.productservice.dataAccess.domain.Product;
import com.manguerasjc.productservice.dataAccess.repositories.*;
import com.manguerasjc.productservice.services.DTO.mapper.ProductMapper;
import com.manguerasjc.productservice.services.DTO.request.ProductRequestDTO;
import com.manguerasjc.productservice.services.DTO.response.ProductResponseDTO;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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
    IMaterialRepository materialRepository;
    @Autowired
    ProductMapper productMapper;



    @Override
    public ProductResponseDTO addProduct(ProductRequestDTO productRequestDTO) {
        Product product = productMapper.toEntity(productRequestDTO);

        // Validar si categoría existe
        product.setCategory(
                categoryRepository.findById(productRequestDTO.categoryId()).
                        orElseThrow(() -> new EntityNotFoundException("Categoria no encontrada"))
        );

        // Validar si la talla existe
        product.setSize(
                sizeRepository.findById(productRequestDTO.sizeId()).
                        orElseThrow(() -> new EntityNotFoundException("Talla no encontrada"))
        );

        // Validar si el material existe
        product.setMaterial(
                materialRepository.findById(productRequestDTO.materialId()).
                        orElseThrow(() -> new EntityNotFoundException("Material no encontrado"))
        );

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

        // Validar si la talla existe
        productEntity.setSize(
                sizeRepository.findById(productRequestDTO.sizeId()).
                        orElseThrow(() -> new EntityNotFoundException("Talla no encontrada"))
        );

        // Validar si el material existe
        productEntity.setMaterial(
                materialRepository.findById(productRequestDTO.materialId()).
                        orElseThrow(() -> new EntityNotFoundException("Material no encontrado"))
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
