//package com.training.InventoryManagement.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.jdbc.datasource.DriverManagerDataSource;
//import javax.sql.DataSource;
//
//@Configuration
//public class JDBCConfig {
//
//    @Bean
//    public DataSource dataSource() {
//        DriverManagerDataSource dataSource = new DriverManagerDataSource();
//        dataSource.setDriverClassName("org.postgresql.Driver");
//        dataSource.setUrl("jdbc:postgresql://localhost:5432/SpringBoot_Training");
//        dataSource.setUsername("srimayuriannadurai");
//        dataSource.setPassword("Skcet@143");
//        return dataSource;
//    }
//}