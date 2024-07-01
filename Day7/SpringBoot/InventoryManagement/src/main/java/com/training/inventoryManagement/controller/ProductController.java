package com.training.inventoryManagement.controller;

import com.training.inventoryManagement.dto.ProductDTO;
import com.training.inventoryManagement.service.serviceInterface.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private ProductService productService;

    @PostMapping("/add")
    public ResponseEntity<String> addProduct(@RequestBody ProductDTO productDTO) {
        return productService.addProduct(productDTO);
    }

    @GetMapping("/prefix/{prefix}")
    public ResponseEntity<?> findByPrefix(@PathVariable String prefix) {
        return productService.findByPrefix(prefix);
    }

    @GetMapping("/prefix/{prefix}/quantityGT/{quantity}")
    public ResponseEntity<?> findByProductNameAndQuantityGT(@PathVariable String prefix, @PathVariable int quantity) {
        return productService.findByProductNameAndQuantityGT(prefix, quantity);
    }

    @GetMapping("/getById/{productId}")
    public ResponseEntity<?> getProductById(@PathVariable Long productId) {
        ProductDTO productDTO = productService.getProductById(productId);
        if (productDTO != null) {
            return ResponseEntity.ok(productDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/delete/{productId}")
    public ResponseEntity<String> deleteProduct(@PathVariable Long productId) {
        return productService.deleteProduct(productId);
    }

    @GetMapping("/getAll")
    public ResponseEntity<?> getAllProducts() {
        return productService.getAllProducts();
    }

    @PutMapping("/update")
    public ResponseEntity<String> updateProduct(@RequestBody ProductDTO productDTO) {
        return productService.updateProduct(productDTO);
    }

    @PutMapping("/update/{productId}/{stocks}")
    public ResponseEntity<String> updateStock(@PathVariable long productId, @PathVariable int stocks) {
        return productService.updateStock(productId, stocks);
    }
}