package com.training.InventoryManagement.dto;

import com.training.InventoryManagement.entity.Product;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDTO {
    private long productId;
    private String productName;
    private double productPrice;
    private int quantity;
    private CategoryDTO category;
    private SellerDetailsDTO seller;

    public static ProductDTO fromProduct(Product product) {
        ProductDTO productDTO = new ProductDTO();
        productDTO.setProductId(product.getProductId());
        productDTO.setProductName(product.getProductName());
        productDTO.setProductPrice(product.getProductPrice());
        productDTO.setQuantity(product.getQuantity());
        return productDTO;
    }
}