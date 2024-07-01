package com.training.OrderManagement.service.serviceInterface;

import com.training.OrderManagement.dto.OrderDTO;
import org.springframework.http.ResponseEntity;
import java.util.List;

public interface OrderService {
    ResponseEntity<String> placeOrder(String userId);
    List<OrderDTO> getAllOrders();
    List<OrderDTO> getOrdersByUserId(String userId);
    OrderDTO getOrderById(String orderId);
}