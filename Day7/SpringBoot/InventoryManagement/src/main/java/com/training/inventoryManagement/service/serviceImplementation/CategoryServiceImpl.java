package com.training.inventoryManagement.service.serviceImplementation;

import com.training.inventoryManagement.dto.CategoryDTO;
import com.training.inventoryManagement.dao.entity.Category;
import com.training.inventoryManagement.dao.repository.CategoryRepository;
import com.training.inventoryManagement.service.serviceInterface.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    @CachePut(value = "categories", key = "#categoryDTO.categoryId")
    public ResponseEntity<String> addCategory(CategoryDTO categoryDTO) {
        Category category = convertToCategory(categoryDTO);
        categoryRepository.save(category);
        return ResponseEntity.status(HttpStatus.OK).body("Category added successfully");
    }

    @Override
    public ResponseEntity<List<CategoryDTO>> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        if (categories.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        List<CategoryDTO> categoryDTOs = categories.stream()
                .map(CategoryDTO::fromCategory)
                .collect(Collectors.toList());
        return ResponseEntity.ok(categoryDTOs);
    }

    @Override
    @CachePut(value = "categories", key = "#categoryDTO.categoryId")
    public ResponseEntity<String> updateCategory(CategoryDTO categoryDTO) {
        Category category = convertToCategory(categoryDTO);
        if (categoryRepository.existsById(category.getCategoryId())) {
            categoryRepository.save(category);
            return ResponseEntity.status(HttpStatus.OK).body("Category updated successfully");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Category not found");
    }

    @Override
    public ResponseEntity<CategoryDTO> getCategoryById(int categoryId) {
        Optional<Category> categoryOptional = categoryRepository.findById(categoryId);
        if (categoryOptional.isPresent()) {
            CategoryDTO categoryDTO = CategoryDTO.fromCategory(categoryOptional.get());
            return ResponseEntity.ok(categoryDTO);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

    @Override
    @CacheEvict(value = "categories", key = "#categoryId")
    public ResponseEntity<String> deleteCategory(int categoryId) {
        if (categoryRepository.existsById(categoryId)) {
            categoryRepository.deleteById(categoryId);
            return ResponseEntity.status(HttpStatus.OK).body("Category deleted successfully");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Category not found");
    }

    private Category convertToCategory(CategoryDTO categoryDTO) {
        Category category = new Category();
        category.setCategoryId(categoryDTO.getCategoryId());
        category.setCategoryName(categoryDTO.getCategoryName());
        return category;
    }
}