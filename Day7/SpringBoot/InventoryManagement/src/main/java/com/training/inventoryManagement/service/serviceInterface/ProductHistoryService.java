package com.training.inventoryManagement.service.serviceInterface;

import org.springframework.http.ResponseEntity;

public interface ProductHistoryService {
    ResponseEntity<?> getProductHistoryByProductId(long productId);
    void saveProductHistory(long productId, String columnName, String oldValue, String newValue);
}
