package com.training.inventoryManagement.dto;

import com.training.inventoryManagement.dao.entity.ProductHistory;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductHistoryDTO {
    private int historyId;
    private String oldValue;
    private String newValue;
    private String columnName;
    private long productId;
    private LocalDate modifiedOn;

    public static ProductHistoryDTO fromProductHistory(ProductHistory productHistory) {
        ProductHistoryDTO productHistoryDTO = new ProductHistoryDTO();
        productHistoryDTO.setHistoryId(productHistory.getHistoryId());
        productHistoryDTO.setOldValue(productHistory.getOldValue());
        productHistoryDTO.setNewValue(productHistory.getNewValue());
        productHistoryDTO.setColumnName(productHistory.getColumnName());
        productHistoryDTO.setProductId(productHistory.getProductId());
        productHistoryDTO.setModifiedOn(productHistory.getModifiedOn());
        return productHistoryDTO;
    }
}