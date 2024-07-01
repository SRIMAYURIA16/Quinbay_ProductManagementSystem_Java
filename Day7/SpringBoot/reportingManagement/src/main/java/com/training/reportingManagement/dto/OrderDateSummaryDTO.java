package com.training.reportingManagement.dto;

import com.training.reportingManagement.dao.entity.Order_Details;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDateSummaryDTO {
    private LocalDate date;
    private int totalProducts;
    private double totalIncome;
    private List<Order_Details> orderDetailsList;
}