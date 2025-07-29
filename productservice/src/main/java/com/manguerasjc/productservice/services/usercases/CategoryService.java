package com.manguerasjc.productservice.services.usercases;

import com.manguerasjc.productservice.dataAccess.domain.Category;
import com.manguerasjc.productservice.dataAccess.repositories.ICategoryRepository;
import com.manguerasjc.productservice.services.DTO.request.CategoryRequestDTO;
import com.manguerasjc.productservice.services.DTO.response.CategoryResponseDTO;
import com.manguerasjc.productservice.services.DTO.mapper.CategoryMapper;
import com.manguerasjc.productservice.services.exceptions.GlobalDefaultExceptionHandler;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CategoryService implements ICategoryService {

    @Autowired
    private ICategoryRepository categoryRepository;
    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    public CategoryResponseDTO addCategory(CategoryRequestDTO category) {
        Category c = categoryMapper.toEntity(category);
        if(categoryRepository.existsByName(c.getName())){
            throw new DataIntegrityViolationException("Categoria existente");
        }
        c = categoryRepository.save(c);
        return categoryMapper.toResponseDTO(c);
    }

    @Override
    public CategoryResponseDTO updateCategory(Long id, CategoryRequestDTO category) {
        Category c = categoryRepository.findById(id).orElse(null);
        if(c == null){
            throw new EntityNotFoundException("La categoria no existe");
        }
        c = categoryMapper.toEntity(category);
        c.setId(id);

        Optional<Category> existing = categoryRepository.findByName(c.getName());
        if(existing.isPresent() && !existing.get().getId().equals(id)){
            throw new DataIntegrityViolationException("Ya existe una categoría con ese nombre");
        }

        return categoryMapper.toResponseDTO(categoryRepository.save(c));
    }

    @Override
    public void deleteCategory(Long id) {
        if(!categoryRepository.existsById(id)){
            throw new EntityNotFoundException("La categoria no existe");
        }
        categoryRepository.deleteById(id);

    }

    @Override
    public List<CategoryResponseDTO> getCategories(){
        return categoryRepository.findAll().
                stream().map(c -> categoryMapper.toResponseDTO(c)).collect(Collectors.toList());
    }
}
