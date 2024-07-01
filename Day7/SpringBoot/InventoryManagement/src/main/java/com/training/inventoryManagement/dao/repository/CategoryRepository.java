package com.training.inventoryManagement.dao.repository;

import com.training.inventoryManagement.dao.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category,Integer> {
}
