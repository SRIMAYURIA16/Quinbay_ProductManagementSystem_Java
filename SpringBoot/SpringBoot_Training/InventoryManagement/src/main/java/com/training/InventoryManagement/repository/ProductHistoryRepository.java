package com.training.InventoryManagement.repository;

import com.training.InventoryManagement.entity.ProductHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ProductHistoryRepository extends JpaRepository<ProductHistory, Long> {
    List<ProductHistory> findByProductId(long productId);
}