package com.training.OrderManagement.controller;

import com.training.OrderManagement.entity.Order;
import com.training.OrderManagement.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.List;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/{userId}/place")
    public ResponseEntity<?> placeOrder(@PathVariable("userId") String userId) {
        return orderService.placeOrder(userId);
    }

    @GetMapping("/getAll")
    public ResponseEntity<?> getAllOrders() {
        try{
            List<Order> orders = orderService.getAllOrders();
            return ResponseEntity.ok(orders);
        }
        catch(RuntimeException exception){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to get all orders "+exception);
        }
    }

    @GetMapping("/getByUserId/{userId}")
    public ResponseEntity<?> getOrdersByUserId(@PathVariable("userId") String userId) {
        try{
            List<Order> orders = orderService.getOrdersByUserId(userId);
            return ResponseEntity.ok(orders);
        }
        catch(RuntimeException exception){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to get all orders "+exception);
        }
    }

    @GetMapping("/getById/{orderId}")
    public ResponseEntity<?> getOrderById(@PathVariable("orderId") String orderId) {
        try{
            Order order = orderService.getOrderById(orderId);
            return ResponseEntity.ok(order);
        }
        catch (RuntimeException exception){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to get all orders "+exception);
        }
    }

    @PostMapping("/cache/{key}/{value}")
    public String redisCache(@PathVariable("key") String key,@PathVariable("value") String value){
        return value;
    }
}