package com.training.reportingManagement.controller;

import com.training.reportingManagement.dao.entity.Order_Items;
import com.training.reportingManagement.service.serviceInterface.OrderItemsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/report/order-items")
public class OrderItemsController {

    @Autowired
    private OrderItemsService orderItemsService;

    @GetMapping("/product/id/{productId}")
    public List<Order_Items> getOrderItemsByProductId(@PathVariable long productId) {
        return orderItemsService.getOrderItemsByProductId(productId);
    }

    @GetMapping("/product/name/{productName}")
    public List<Order_Items> getOrderItemsByProductName(@PathVariable String productName) {
        return orderItemsService.getOrderItemsByProductName(productName);
    }
}