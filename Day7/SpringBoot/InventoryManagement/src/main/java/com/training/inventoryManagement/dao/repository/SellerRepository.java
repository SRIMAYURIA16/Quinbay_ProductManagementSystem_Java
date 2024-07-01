package com.training.inventoryManagement.dao.repository;

import com.training.inventoryManagement.dao.entity.SellerDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SellerRepository extends JpaRepository<SellerDetails, Integer> {
}