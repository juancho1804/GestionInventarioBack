package com.manguerasjc.productservice.services.usercases;

import com.manguerasjc.productservice.services.DTO.request.CategoryRequestDTO;
import com.manguerasjc.productservice.services.DTO.request.ProductRequestDTO;
import com.manguerasjc.productservice.services.DTO.response.CategoryResponseDTO;
import com.manguerasjc.productservice.services.DTO.response.ProductResponseDTO;

import java.util.List;

public interface IProductService {
    ProductResponseDTO addProduct(ProductRequestDTO productRequestDTO);
    ProductResponseDTO updateProduct(Long id, ProductRequestDTO productRequestDTO);
    void deleteProduct(Long id);
    List<ProductResponseDTO> getProducts();
    List<ProductResponseDTO>getProductsByCategoryId(Long categoryId);
    List<ProductResponseDTO>findProductsWithFilters(List<Long> categoryId, List<Long> brandId, List <Long>SizesIds);

}
