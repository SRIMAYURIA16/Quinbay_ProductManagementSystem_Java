package com.training.inventoryManagement.dto;

import com.training.inventoryManagement.dao.entity.Product;
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
    private int categoryId;
    private int sellerId;

    public static ProductDTO fromProduct(Product product) {
        ProductDTO productDTO = new ProductDTO();
        productDTO.setProductId(product.getProductId());
        productDTO.setProductName(product.getProductName());
        productDTO.setProductPrice(product.getProductPrice());
        productDTO.setQuantity(product.getQuantity());
        productDTO.setCategoryId(product.getCategory().getCategoryId());
        productDTO.setSellerId(product.getSeller().getId());
        return productDTO;
    }
}
