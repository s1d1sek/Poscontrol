package com.ordersystem.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class OrderSystemBackendApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(OrderSystemBackendApplication.class, args);
        System.out.println("\n=================================");
        System.out.println("Backend is running on port 8080!");
        System.out.println("API available at: http://localhost:8080/api/test");
        System.out.println("=================================\n");
    }
}
