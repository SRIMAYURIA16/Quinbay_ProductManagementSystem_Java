package com.training.InventoryManagement.Service;

import com.training.InventoryManagement.dto.ProductHistoryDTO;
import com.training.InventoryManagement.entity.ProductHistory;
import com.training.InventoryManagement.repository.ProductHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductHistoryService {

    @Autowired
    private ProductHistoryRepository productHistoryRepository;

    public ResponseEntity<?> getProductHistoryByProductId(long productId) {
        List<ProductHistory> productHistories = productHistoryRepository.findByProductId(productId);
        List<ProductHistoryDTO> productHistoryDTOS=productHistories.stream().
                map(ProductHistoryDTO::fromProductHistory).collect(Collectors.toList());
        return ResponseEntity.ok(productHistoryDTOS);
    }

    public void saveProductHistory(long productId, String columnName, String oldValue, String newValue) {
        ProductHistory productHistory = new ProductHistory();
        productHistory.setProductId(productId);
        productHistory.setColumnName(columnName);
        productHistory.setOldValue(oldValue);
        productHistory.setNewValue(newValue);
        productHistory.setModifiedOn(LocalDate.now());
        productHistoryRepository.save(productHistory);
    }
}