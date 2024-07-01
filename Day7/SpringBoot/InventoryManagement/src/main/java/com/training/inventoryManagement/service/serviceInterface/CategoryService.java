package com.training.inventoryManagement.service.serviceInterface;

import com.training.inventoryManagement.dto.CategoryDTO;
import org.springframework.http.ResponseEntity;
import java.util.List;

public interface CategoryService {
    ResponseEntity<String> addCategory(CategoryDTO categoryDTO);
    ResponseEntity<List<CategoryDTO>> getAllCategories();
    ResponseEntity<String> updateCategory(CategoryDTO categoryDTO);
    ResponseEntity<CategoryDTO> getCategoryById(int categoryId);
    ResponseEntity<String> deleteCategory(int categoryId);
}