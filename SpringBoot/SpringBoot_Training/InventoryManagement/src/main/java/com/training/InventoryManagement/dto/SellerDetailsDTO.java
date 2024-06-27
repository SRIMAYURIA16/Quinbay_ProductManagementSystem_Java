package com.training.InventoryManagement.dto;

import com.training.InventoryManagement.entity.SellerDetails;
import jakarta.persistence.CascadeType;
import jakarta.persistence.OneToMany;
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
    @OneToMany(mappedBy = "seller",cascade = CascadeType.ALL)
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