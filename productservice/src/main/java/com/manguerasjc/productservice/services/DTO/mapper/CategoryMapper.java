package com.manguerasjc.productservice.services.DTO.mapper;

import com.manguerasjc.productservice.dataAccess.domain.Category;
import com.manguerasjc.productservice.services.DTO.request.CategoryRequestDTO;
import com.manguerasjc.productservice.services.DTO.response.CategoryResponseDTO;
import org.springframework.stereotype.Component;

@Component
public class CategoryMapper {
    public Category toEntity(CategoryRequestDTO dto) {
        Category c = new Category();
        c.setName(dto.name());
        return c;
    }

    public CategoryResponseDTO toResponseDTO(Category c) {
        return new CategoryResponseDTO(c.getId(), c.getName());
    }

}
