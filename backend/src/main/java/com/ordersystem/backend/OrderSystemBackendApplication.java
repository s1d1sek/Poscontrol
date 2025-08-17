package com.ordersystem.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class OrderSystemBackendApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(OrderSystemBackendApplication.class, args);
        System.out.println("Application is running on http://localhost:8080");
    }
}
