package com.training.OrderManagement.dao.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "Orders")
public class Order {
    @Id
    private String id;
    private String userId;
    private List<Product> productList;
    private double totalPrice;
    private int totalProducts;
}