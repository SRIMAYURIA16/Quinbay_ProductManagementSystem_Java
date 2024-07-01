package com.training.OrderManagement.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDTO {
    private String id;
    private String userId;
    private List<ProductDTO> productList;
    private double totalPrice;
    private int totalProducts;
}