package com.training.inventoryManagement.service.serviceImplementation;

import com.training.inventoryManagement.dto.SellerDetailsDTO;
import com.training.inventoryManagement.dao.entity.SellerDetails;
import com.training.inventoryManagement.dao.repository.SellerRepository;
import com.training.inventoryManagement.service.serviceInterface.SellerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class SellerServiceImpl implements SellerService {

    @Autowired
    private SellerRepository sellerRepository;

    @Override
    public ResponseEntity<String> addSeller(SellerDetailsDTO sellerDetailsDTO) {
        SellerDetails sellerDetails = convertToSellerDetails(sellerDetailsDTO);
        sellerRepository.save(sellerDetails);
        return ResponseEntity.status(HttpStatus.OK).body("Seller added successfully");
    }

    @Override
    public ResponseEntity<?> getSellerById(int sellerId) {
        Optional<SellerDetails> sellerDetails = sellerRepository.findById(sellerId);
        if (sellerDetails.isPresent()) {
            SellerDetailsDTO sellerDetailsDTO = SellerDetailsDTO.fromSeller(sellerDetails.get());
            return ResponseEntity.ok(sellerDetailsDTO);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Seller not found with id" + sellerId);
    }

    @Override
    public ResponseEntity<?> getAllSellers() {
        List<SellerDetails> sellerDetailsList = sellerRepository.findAll();
        List<SellerDetailsDTO> sellerDTOs = sellerDetailsList.stream()
                .map(SellerDetailsDTO::fromSeller)
                .collect(Collectors.toList());
        return ResponseEntity.ok(sellerDTOs);
    }

    @Override
    public ResponseEntity<String> updateSeller(SellerDetailsDTO sellerDetailsDTO) {
        SellerDetails sellerDetails = convertToSellerDetails(sellerDetailsDTO);
        Optional<SellerDetails> existingSeller = sellerRepository.findById(sellerDetails.getId());
        if (existingSeller.isPresent()) {
            sellerRepository.save(sellerDetails);
            return ResponseEntity.status(HttpStatus.OK).body("Updated the seller");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Seller not found with id" + sellerDetails.getId());
    }

    @Override
    public ResponseEntity<String> deleteSeller(int sellerId) {
        if (sellerRepository.existsById(sellerId)) {
            sellerRepository.deleteById(sellerId);
            return ResponseEntity.status(HttpStatus.OK).body("Seller deleted ");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Seller is not found with the id:" + sellerId);
    }

    private SellerDetails convertToSellerDetails(SellerDetailsDTO sellerDetailsDTO) {
        SellerDetails sellerDetails = new SellerDetails();
        sellerDetails.setId(sellerDetailsDTO.getId());
        sellerDetails.setName(sellerDetailsDTO.getName());
        sellerDetails.setMobileNo(sellerDetailsDTO.getMobileNo());
        sellerDetails.setAddress(sellerDetailsDTO.getAddress());
        return sellerDetails;
    }
}