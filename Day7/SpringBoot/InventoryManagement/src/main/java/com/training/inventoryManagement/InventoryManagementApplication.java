package com.training.inventoryManagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories
@EnableCaching
@EnableDiscoveryClient
public class InventoryManagementApplication {
	public static void main(String[] args) {
		SpringApplication.run(InventoryManagementApplication.class, args);
	}

}
