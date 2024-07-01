package com.training.reportingManagement.dao.repository;

import com.training.reportingManagement.dao.entity.Order_Items;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface OrderItemsRepository extends JpaRepository<Order_Items, Integer> {
    List<Order_Items> findByProductId(long productId);
    List<Order_Items> findByProductName(String productName);
}