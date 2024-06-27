//package com.training.OrderManagement.Config;
//
//
//import com.mongodb.ConnectionString;
//import com.mongodb.client.MongoClient;
//import com.mongodb.client.MongoClients;
//import com.mongodb.client.MongoDatabase;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//public class MongoConfig {
//
//    @Bean
//    public MongoDatabase mongoDatabase() {
//        MongoClient mongoClient=MongoClients.create("mongodb://localhost:27017/SpringBoot_Training");
//        MongoDatabase mongoDatabase=mongoClient.getDatabase("SpringBoot_Training");
//
//        return mongoDatabase;
//    }
//}
