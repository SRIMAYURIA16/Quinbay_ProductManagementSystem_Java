package com.training.InventoryManagement.Service;

import com.training.InventoryManagement.dto.CategoryDTO;
import com.training.InventoryManagement.entity.Category;
import com.training.InventoryManagement.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    public ResponseEntity<String> addCategory(CategoryDTO categoryDTO) {
        Category category = convertToCategory(categoryDTO);
        categoryRepository.save(category);
        return ResponseEntity.status(HttpStatus.OK).body("Category added successfully");
    }

    public ResponseEntity<?> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        if (categories.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No categories available");
        }
        List<CategoryDTO> categoryDTOs = categories.stream()
                .map(CategoryDTO::fromCategory)
                .collect(Collectors.toList());
        return ResponseEntity.ok(categoryDTOs);
    }

    public ResponseEntity<String> updateCategory(CategoryDTO categoryDTO) {
        Category category = convertToCategory(categoryDTO);
        if (categoryRepository.existsById(category.getCategoryId())) {
            categoryRepository.save(category);
            return ResponseEntity.status(HttpStatus.OK).body("Category updated successfully");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Category not found");
    }

    public ResponseEntity<?> getCategoryById(int categoryId) {
        Optional<Category> categoryOptional = categoryRepository.findById(categoryId);
        if (categoryOptional.isPresent()) {
            CategoryDTO categoryDTO = CategoryDTO.fromCategory(categoryOptional.get());
            return ResponseEntity.ok(categoryDTO);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Category not found");
    }

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
