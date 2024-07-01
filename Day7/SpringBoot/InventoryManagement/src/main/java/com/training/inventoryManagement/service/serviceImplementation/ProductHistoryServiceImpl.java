package com.training.inventoryManagement.service.serviceImplementation;

import com.training.inventoryManagement.dto.ProductHistoryDTO;
import com.training.inventoryManagement.dao.entity.ProductHistory;
import com.training.inventoryManagement.dao.repository.ProductHistoryRepository;
import com.training.inventoryManagement.service.serviceInterface.ProductHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductHistoryServiceImpl implements ProductHistoryService {

    @Autowired
    private ProductHistoryRepository productHistoryRepository;

    @Override
    public ResponseEntity<?> getProductHistoryByProductId(long productId) {
        List<ProductHistory> productHistories = productHistoryRepository.findByProductId(productId);
        List<ProductHistoryDTO> productHistoryDTOS = productHistories.stream()
                .map(ProductHistoryDTO::fromProductHistory)
                .collect(Collectors.toList());
        return ResponseEntity.ok(productHistoryDTOS);
    }

    @Override
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