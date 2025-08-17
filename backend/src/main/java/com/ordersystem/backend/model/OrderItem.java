package com.ordersystem.backend.model;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "order_items")
public class OrderItem {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    // @ManyToOne creates a relationship: many order items can belong to one order
    // @JoinColumn specifies the foreign key column name in the database
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;
    
    // @ManyToOne: many order items can reference the same product
    @ManyToOne(fetch = FetchType.EAGER) // EAGER because we usually need product info immediately
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;
    
    // How many of this product were ordered
    @Column(nullable = false)
    private Integer quantity;
    
    // Price of the product at the time of order (important for price history)
    // We store the price here in case the product price changes later
    @Column(nullable = false)
    private BigDecimal unitPrice;
    
    // Default constructor
    public OrderItem() {}
    
    // Constructor for creating order items
    public OrderItem(Order order, Product product, Integer quantity, BigDecimal unitPrice) {
        this.order = order;
        this.product = product;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
    }
    
    // Getter and Setter methods
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Order getOrder() {
        return order;
    }
    
    public void setOrder(Order order) {
        this.order = order;
    }
    
    public Product getProduct() {
        return product;
    }
    
    public void setProduct(Product product) {
        this.product = product;
    }
    
    public Integer getQuantity() {
        return quantity;
    }
    
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
    
    public BigDecimal getUnitPrice() {
        return unitPrice;
    }
    
    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }
    
    // Helper method to calculate the total price for this line item
    public BigDecimal getTotalPrice() {
        return unitPrice.multiply(BigDecimal.valueOf(quantity));
    }
    
    @Override
    public String toString() {
        return "OrderItem{" +
                "id=" + id +
                ", product=" + product.getName() +
                ", quantity=" + quantity +
                ", unitPrice=" + unitPrice +
                '}';
    }
}