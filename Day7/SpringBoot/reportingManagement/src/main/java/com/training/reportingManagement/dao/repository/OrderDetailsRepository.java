package com.training.reportingManagement.dao.repository;

import com.training.reportingManagement.dao.entity.Order_Details;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface OrderDetailsRepository extends JpaRepository <Order_Details,Integer> {
    List<Order_Details> findByEmailId(String emailId);
    List<Order_Details> findByCreatedDate(LocalDate createdDate);
}