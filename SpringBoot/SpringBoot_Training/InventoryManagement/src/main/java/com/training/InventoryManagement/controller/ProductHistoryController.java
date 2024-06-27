package com.training.InventoryManagement.controller;

import com.training.InventoryManagement.dto.ProductHistoryDTO;
import com.training.InventoryManagement.Service.ProductHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("productHistory")
public class ProductHistoryController {

    @Autowired
    private ProductHistoryService productHistoryService;

    @GetMapping("/getByProductId/{productId}")
    public ResponseEntity<?> getProductHistoryByProductId(@PathVariable long productId) {
        return productHistoryService.getProductHistoryByProductId(productId);
    }
}
