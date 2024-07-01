package com.training.inventoryManagement.controller;
import com.training.inventoryManagement.dto.CategoryDTO;
import com.training.inventoryManagement.service.serviceInterface.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import java.util.List;

@RestController
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private  CategoryService categoryService;

    @PostMapping("/add")
    public ResponseEntity<String> addCategory(@RequestBody CategoryDTO categoryDTO) {
        return categoryService.addCategory(categoryDTO);
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<CategoryDTO>> getAllCategories() {
        return categoryService.getAllCategories();
    }

    @GetMapping("/get/{categoryId}")
    public ResponseEntity<CategoryDTO> getCategoryById(@PathVariable int categoryId) {
        return categoryService.getCategoryById(categoryId);
    }

    @PutMapping("/update")
    public ResponseEntity<String> updateCategory(@RequestBody CategoryDTO categoryDTO) {
        return categoryService.updateCategory(categoryDTO);
    }

    @DeleteMapping("/remove/{categoryId}")
    public ResponseEntity<String> deleteCategory(@PathVariable int categoryId) {
        return categoryService.deleteCategory(categoryId);
    }
}