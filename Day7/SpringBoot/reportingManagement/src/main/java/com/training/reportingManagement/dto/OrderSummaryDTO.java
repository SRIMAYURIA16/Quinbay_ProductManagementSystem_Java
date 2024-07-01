package com.training.reportingManagement.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderSummaryDTO {
    private String emailId;
    private int totalProducts;
    private double totalAmount;
}
