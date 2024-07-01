package com.training.inventoryManagement.dto;

import com.training.inventoryManagement.dao.entity.SellerDetails;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SellerDetailsDTO {
    private int id;
    private String name;
    private long mobileNo;
    private String address;
    private List<ProductDTO> productList;

    public static SellerDetailsDTO fromSeller(SellerDetails sellerDetails) {
        SellerDetailsDTO sellerDetailsDTO = new SellerDetailsDTO();
        sellerDetailsDTO.setId(sellerDetails.getId());
        sellerDetailsDTO.setName(sellerDetails.getName());
        sellerDetailsDTO.setMobileNo(sellerDetails.getMobileNo());
        sellerDetailsDTO.setAddress(sellerDetails.getAddress());
        if (sellerDetails.getProductList() != null) {
            sellerDetailsDTO.setProductList(sellerDetails.getProductList().stream()
                    .map(ProductDTO::fromProduct)
                    .collect(Collectors.toList()));
        }
        return sellerDetailsDTO;
    }
}