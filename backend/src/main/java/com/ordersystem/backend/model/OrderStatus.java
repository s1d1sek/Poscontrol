package com.ordersystem.backend.model;

// Enum defines a fixed set of constants
// This represents the different states an order can be in
public enum OrderStatus {
    PENDING,    // Order received but not processed
    CONFIRMED,  // Order confirmed and being prepared
    COMPLETED,  // Order finished and delivered
    CANCELLED   // Order was cancelled
}