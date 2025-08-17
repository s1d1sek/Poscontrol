package com.ordersystem.backend.dto;

import java.util.List;

// DTO (Data Transfer Object) for receiving order creation requests
public class OrderRequest {
    private String customerName;
    private String customerEmail;
    private List<OrderItemRequest> orderItems;
    
    // Default constructor
    public OrderRequest() {}
    
    // Constructor with parameters
    public OrderRequest(String customerName, String customerEmail, List<OrderItemRequest> orderItems) {
        this.customerName = customerName;
        this.customerEmail = customerEmail;
        this.orderItems = orderItems;
    }
    
    // Getters and Setters
    public String getCustomerName() {
        return customerName;
    }
    
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }
    
    public String getCustomerEmail() {
        return customerEmail;
    }
    
    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }
    
    public List<OrderItemRequest> getOrderItems() {
        return orderItems;
    }
    
    public void setOrderItems(List<OrderItemRequest> orderItems) {
        this.orderItems = orderItems;
    }
}