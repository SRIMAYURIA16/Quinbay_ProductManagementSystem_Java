package com.training.OrderManagement.controller;

import com.training.OrderManagement.dto.CartDTO;
import com.training.OrderManagement.service.serviceInterface.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @PostMapping("/{userId}/add/{productId}/{quantity}")
    public ResponseEntity<String> addProductToCart(@PathVariable String userId, @PathVariable long productId, @PathVariable int quantity) {
        return cartService.addProductToCart(userId, productId, quantity);
    }

    @GetMapping("/get/{userId}")
    public ResponseEntity<CartDTO> getCart(@PathVariable String userId) {
        return cartService.getCart(userId);
    }

    @DeleteMapping("/{userId}/remove/{productId}")
    public ResponseEntity<String> removeFromCart(@PathVariable String userId, @PathVariable long productId) {
        return cartService.removeFromCart(userId, productId);
    }

    @DeleteMapping("/{userId}/clear")
    public ResponseEntity<String> clearCart(@PathVariable String userId) {
        return cartService.clearCart(userId);
    }

    @PutMapping("/{userId}/update/{productId}/{quantity}")
    public ResponseEntity<String> updateProductStocksInCart(@PathVariable String userId, @PathVariable long productId, @PathVariable int quantity) {
        return cartService.updateProductStocksInCart(userId, productId, quantity);
    }
}