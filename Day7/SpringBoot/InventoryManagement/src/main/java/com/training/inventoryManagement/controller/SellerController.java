package com.training.inventoryManagement.controller;

import com.training.inventoryManagement.dto.SellerDetailsDTO;
import com.training.inventoryManagement.service.serviceInterface.SellerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/seller")
public class SellerController {

    @Autowired
    private SellerService sellerService;

    @PostMapping("/add")
    public ResponseEntity<String> addSeller(@RequestBody SellerDetailsDTO sellerDetailsDTO) {
        return sellerService.addSeller(sellerDetailsDTO);
    }

    @PutMapping("/update")
    public ResponseEntity<String> updateSeller(@RequestBody SellerDetailsDTO sellerDetailsDTO) {
        return sellerService.updateSeller(sellerDetailsDTO);
    }

    @DeleteMapping("/remove/{id}")
    public ResponseEntity<?> deleteSeller(@PathVariable int id) {
        return sellerService.deleteSeller(id);
    }

    @GetMapping("/getById/{id}")
    public ResponseEntity<?> getSellerById(@PathVariable int id) {
        return sellerService.getSellerById(id);
    }

    @GetMapping("/getAll")
    public ResponseEntity<?> getAllSellers() {
        return sellerService.getAllSellers();
    }
}