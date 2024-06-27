package com.training.InventoryManagement.controller;

import com.training.InventoryManagement.dto.CategoryDTO;
import com.training.InventoryManagement.Service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @PostMapping("/add")
    public ResponseEntity<String> addCategory(@RequestBody CategoryDTO categoryDTO) {
        return categoryService.addCategory(categoryDTO);
    }

    @GetMapping("/getAll")
    public ResponseEntity<?> getAllCategories() {
        return categoryService.getAllCategories();
    }

    @PutMapping("/update")
    public ResponseEntity<String> updateCategory(@RequestBody CategoryDTO categoryDTO) {
        return categoryService.updateCategory(categoryDTO);
    }

    @GetMapping("/getById/{categoryId}")
    public ResponseEntity<?> getCategoryById(@PathVariable("categoryId") int categoryId) {
        return categoryService.getCategoryById(categoryId);
    }

    @DeleteMapping("/delete/{categoryId}")
    public ResponseEntity<String> deleteCategory(@PathVariable("categoryId") int categoryId) {
        return categoryService.deleteCategory(categoryId);
    }
}
