package com.manguerasjc.productservice.controllers;

import com.manguerasjc.productservice.services.DTO.request.CategoryRequestDTO;
import com.manguerasjc.productservice.services.DTO.response.CategoryResponseDTO;
import com.manguerasjc.productservice.services.usercases.ICategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categories")
public class CategoryController {
    @Autowired
    private ICategoryService categoryService;

    @PostMapping
    public CategoryResponseDTO save(@RequestBody @Valid CategoryRequestDTO c) {
        return categoryService.addCategory(c);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }
    @PutMapping("/{id}")
    public CategoryResponseDTO updateCategory(@PathVariable Long id,@RequestBody @Valid CategoryRequestDTO c) {
        return categoryService.updateCategory(id, c);
    }
    @GetMapping
    public List<CategoryResponseDTO> getAllCategories() {
        return categoryService.getCategories();
    }






}
