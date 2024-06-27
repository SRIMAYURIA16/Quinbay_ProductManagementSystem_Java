package com.training.InventoryManagement.controller;

import com.training.InventoryManagement.Service.SellerService;
import com.training.InventoryManagement.dto.SellerDetailsDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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