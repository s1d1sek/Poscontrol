package com.ordersystem.backend.model;

import jakarta.persistence.*;  // JPA annotations
import java.math.BigDecimal;   // For precise decimal calculations
import java.time.LocalDateTime; // For date and time handling
import java.util.ArrayList;    // For dynamic lists
import java.util.List;         // List interface

@Entity
@Table(name = "orders")
public class Order {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    // When the order was created
    @Column(nullable = false)
    private LocalDateTime orderDate;
    
    // Customer information
    @Column(nullable = false)
    private String customerName;
    
    // Email is optional
    private String customerEmail;
    
    // Total amount for the entire order
    @Column(nullable = false)
    private BigDecimal totalAmount;
    
    // @Enumerated tells JPA how to store the enum in the database
    // EnumType.STRING stores the actual string value (PENDING, COMPLETED, etc.)
    @Enumerated(EnumType.STRING)
    private OrderStatus status;
    
    // @OneToMany creates a relationship: one order can have many order items
    // mappedBy = "order" means the OrderItem entity has an "order" field that owns this relationship
    // cascade = CascadeType.ALL means when we save/delete an order, also save/delete its items
    // fetch = FetchType.LAZY means order items are loaded only when needed (better performance)
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<OrderItem> orderItems = new ArrayList<>();
    
    // Default constructor
    public Order() {}
    
    // Constructor for creating new orders
    public Order(String customerName, String customerEmail, OrderStatus status) {
        this.customerName = customerName;
        this.customerEmail = customerEmail;
        this.status = status;
        this.orderDate = LocalDateTime.now(); // Set current date/time
        this.totalAmount = BigDecimal.ZERO;   // Start with zero, calculate later
    }
    
    // Getter and Setter methods
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public LocalDateTime getOrderDate() {
        return orderDate;
    }
    
    public void setOrderDate(LocalDateTime orderDate) {
        this.orderDate = orderDate;
    }
    
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
    
    public BigDecimal getTotalAmount() {
        return totalAmount;
    }
    
    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }
    
    public OrderStatus getStatus() {
        return status;
    }
    
    public void setStatus(OrderStatus status) {
        this.status = status;
    }
    
    public List<OrderItem> getOrderItems() {
        return orderItems;
    }
    
    public void setOrderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }
    
    // Helper method to add an item to this order
    public void addOrderItem(OrderItem orderItem) {
        orderItems.add(orderItem);
        orderItem.setOrder(this); // Set the reverse relationship
    }
    
    // Helper method to calculate total amount from all order items
    public void calculateTotal() {
        this.totalAmount = orderItems.stream()
            .map(item -> item.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
    
    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", orderDate=" + orderDate +
                ", customerName='" + customerName + '\'' +
                ", totalAmount=" + totalAmount +
                ", status=" + status +
                '}';
    }
}