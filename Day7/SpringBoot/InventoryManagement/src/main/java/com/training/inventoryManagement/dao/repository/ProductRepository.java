package com.training.inventoryManagement.dao.repository;

import com.training.inventoryManagement.dao.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    public List<Product> findByProductNameStartingWith(String prefix);
    public List<Product> findByProductNameStartingWithAndQuantityGreaterThan(String prefix,int quantity);
}