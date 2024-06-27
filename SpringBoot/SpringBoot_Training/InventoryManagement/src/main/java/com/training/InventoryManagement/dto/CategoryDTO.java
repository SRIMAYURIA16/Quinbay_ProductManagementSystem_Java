package com.training.InventoryManagement.dto;

import com.training.InventoryManagement.entity.Category;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDTO {
    private int categoryId;
    private String categoryName;
    private List<ProductDTO> products;

    public static CategoryDTO fromCategory(Category category) {
        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setCategoryId(category.getCategoryId());
        categoryDTO.setCategoryName(category.getCategoryName());
        if (category.getProducts() != null) {
            categoryDTO.setProducts(category.getProducts().stream()
                    .map(ProductDTO::fromProduct)
                    .collect(Collectors.toList()));
        }
//        List<ProductDTO> productDTOs = category.getProducts().stream()
//                .map(ProductDTO::fromProduct)
//                .collect(Collectors.toList());
//        categoryDTO.setProducts(productDTOs);
        return categoryDTO;
    }
}