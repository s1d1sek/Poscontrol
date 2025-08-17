package com.ordersystem.backend.dto;

// DTO for individual order items in an order request
public class OrderItemRequest {
    private Long productId;
    private Integer quantity;
    
    // Default constructor
    public OrderItemRequest() {}
    
    // Constructor with parameters
    public OrderItemRequest(Long productId, Integer quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }
    
    // Getters and Setters
    public Long getProductId() {
        return productId;
    }
    
    public void setProductId(Long productId) {
        this.productId = productId;
    }
    
    public Integer getQuantity() {
        return quantity;
    }
    
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}