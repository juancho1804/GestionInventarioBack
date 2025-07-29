package com.manguerasjc.productservice.services.usercases;

import com.manguerasjc.productservice.dataAccess.domain.Category;
import com.manguerasjc.productservice.services.DTO.request.CategoryRequestDTO;
import com.manguerasjc.productservice.services.DTO.response.CategoryResponseDTO;

import java.util.List;

public interface ICategoryService {
    public CategoryResponseDTO addCategory(CategoryRequestDTO category);
    public CategoryResponseDTO updateCategory(Long id, CategoryRequestDTO category);
    public void deleteCategory(Long id);
    public List<CategoryResponseDTO> getCategories();
}
