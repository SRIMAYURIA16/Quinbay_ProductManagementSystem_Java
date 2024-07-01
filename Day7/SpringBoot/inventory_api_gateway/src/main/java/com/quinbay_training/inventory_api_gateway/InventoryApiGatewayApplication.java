package com.quinbay_training.inventory_api_gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class InventoryApiGatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(InventoryApiGatewayApplication.class, args);
	}

}
