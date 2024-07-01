package com.training.reportingManagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.kafka.annotation.EnableKafka;

@EnableKafka
@SpringBootApplication
@EnableDiscoveryClient
public class ReportingManagementApplication {

	public static void main(String[] args) {
		SpringApplication.run(ReportingManagementApplication.class, args);
	}

}
