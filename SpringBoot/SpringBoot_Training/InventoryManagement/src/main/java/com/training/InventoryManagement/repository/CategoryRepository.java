package com.training.InventoryManagement.repository;

import com.training.InventoryManagement.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category,Integer> {
}
