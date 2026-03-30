package com.manguerasjc.productservice.services.usercases;

import com.manguerasjc.productservice.services.DTO.request.CategoryRequestDTO;
import com.manguerasjc.productservice.services.DTO.response.CategoryResponseDTO;

import java.util.List;

public interface ICategoryService {
    CategoryResponseDTO addCategory(CategoryRequestDTO category);
    CategoryResponseDTO updateCategory(Long id, CategoryRequestDTO category);
    void deleteCategory(Long id);
    List<CategoryResponseDTO> getCategories();
}
