package com.training.inventoryManagement.service.serviceInterface;

import com.training.inventoryManagement.dto.SellerDetailsDTO;
import org.springframework.http.ResponseEntity;

public interface SellerService {
    ResponseEntity<String> addSeller(SellerDetailsDTO sellerDetailsDTO);
    ResponseEntity<?> getSellerById(int sellerId);
    ResponseEntity<?> getAllSellers();
    ResponseEntity<String> updateSeller(SellerDetailsDTO sellerDetailsDTO);
    ResponseEntity<String> deleteSeller(int sellerId);
}