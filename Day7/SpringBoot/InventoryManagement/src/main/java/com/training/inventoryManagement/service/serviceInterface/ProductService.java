package com.training.inventoryManagement.service.serviceInterface;

import com.training.inventoryManagement.dto.ProductDTO;
import org.springframework.http.ResponseEntity;

public interface ProductService {
    ResponseEntity<String> addProduct(ProductDTO productDTO);
    ResponseEntity<?> findByPrefix(String prefix);
    ResponseEntity<?> findByProductNameAndQuantityGT(String prefix, int quantity);
    ProductDTO getProductById(Long productId);
    ResponseEntity<String> deleteProduct(Long productId);
    ResponseEntity<?> getAllProducts();
    ResponseEntity<String> updateProduct(ProductDTO productDTO);
    ResponseEntity<String> updateStock(long productId, int stocks);
}