package com.ordersystem.backend.controller;

import org.springframework.web.bind.annotation.*;

// Simple test controller to verify the backend is running
@RestController
@RequestMapping("/api/test")
@CrossOrigin(origins = "http://localhost:4200")
public class TestController {
    
    // GET endpoint: http://localhost:8080/api/test
    // Use this to test if your backend is running correctly
    @GetMapping
    public String testConnection() {
        return "Backend is running successfully!";
    }
    
    // GET endpoint: http://localhost:8080/api/test/hello/{name}
    // Example: http://localhost:8080/api/test/hello/John
    @GetMapping("/hello/{name}")
    public String sayHello(@PathVariable String name) {
        return "Hello, " + name + "! Your POS system backend is working!";
    }
}