package com.training.InventoryManagement.repository;

import com.training.InventoryManagement.entity.SellerDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SellerRepository extends JpaRepository<SellerDetails, Integer> {
}