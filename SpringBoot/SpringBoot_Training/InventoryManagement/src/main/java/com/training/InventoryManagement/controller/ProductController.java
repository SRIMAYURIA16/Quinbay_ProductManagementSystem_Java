package com.training.InventoryManagement.controller;

import com.training.InventoryManagement.dto.ProductDTO;
import com.training.InventoryManagement.Service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private ProductService productService;

    @PostMapping("/add")
    public ResponseEntity<String> addProduct(@RequestBody ProductDTO productDTO) {
        return productService.addProduct(productDTO);
    }

    @GetMapping("/getById/{productId}")
    public ResponseEntity<?> getProductById(@PathVariable("productId") Long productId) {
        return productService.getProductById(productId);
    }

    @GetMapping("/getAll")
    public ResponseEntity<?> getAllProducts() {
        return productService.getAllProducts();
    }

    @PutMapping("/update")
    public ResponseEntity<String> updateProduct(@RequestBody ProductDTO productDTO) {
        return productService.updateProduct(productDTO);
    }

    @GetMapping("/getByPrefix/{prefix}")
    public ResponseEntity<?> getByPrefix(@PathVariable("prefix") String prefix) {
        return productService.findByPrefix(prefix);
    }

    @GetMapping("/getByNameAndQuantityGT/{prefix}/{quantity}")
    public ResponseEntity<?> getByNameAndQuantityGT(@PathVariable("prefix") String prefix, @PathVariable("quantity") int quantity) {
        return productService.findByProductNameAndQuantityGT(prefix, quantity);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable("id") Long productId) {
        return productService.deleteProduct(productId);
    }
}
