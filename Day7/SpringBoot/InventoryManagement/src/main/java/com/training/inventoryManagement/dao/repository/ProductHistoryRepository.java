package com.training.inventoryManagement.dao.repository;

import com.training.inventoryManagement.dao.entity.ProductHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ProductHistoryRepository extends JpaRepository<ProductHistory, Long> {
    List<ProductHistory> findByProductId(long productId);
}