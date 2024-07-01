package com.training.OrderManagement.service.serviceInterface;

import com.training.OrderManagement.dto.CartDTO;
import com.training.OrderManagement.dto.ProductDTO;
import org.springframework.http.ResponseEntity;

public interface CartService {
    ResponseEntity<String> addProductToCart(String userId, long productId, int stocks);
    ResponseEntity<CartDTO> getCart(String userId);
    ResponseEntity<String> removeFromCart(String userId, long productId);
    ResponseEntity<String> clearCart(String userId);
    ResponseEntity<String> updateProductStocksInCart(String userId, long productId, int newStocks);
    ProductDTO getProductDetails(long productId);
}