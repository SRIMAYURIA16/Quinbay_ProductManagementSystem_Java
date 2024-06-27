package com.training.OrderManagement.controller;

import com.training.OrderManagement.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.DeleteMapping;

@RestController
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @PostMapping("/{userId}/add/{productId}/{quantity}")
    public ResponseEntity<String> addProductToCart(@PathVariable String userId, @PathVariable long productId, @PathVariable int quantity) {
        return cartService.addProductToCart(userId,productId,quantity);
    }

    @GetMapping("/get/{userId}")
    public ResponseEntity<?> getCart(@PathVariable String userId) {
        return cartService.getCart(userId);
    }

    @DeleteMapping("/{userId}/remove/{productId}")
    public ResponseEntity<String> removeFromCart(@PathVariable String userId, @PathVariable long productId) {
        return cartService.removeFromCart(userId,productId);
    }

    @DeleteMapping("/{userId}/clear")
    public ResponseEntity<String> clearCart(@PathVariable String userId) {
        return cartService.clearCart(userId);
    }

    @PutMapping("/{userId}/update/{productId}/{quantity}")
    public ResponseEntity<String> updateProductStocksInCart(@PathVariable String userId, @PathVariable long productId, @PathVariable int quantity) {
        return cartService.updateProductStocksInCart(userId,productId,quantity);
    }


}