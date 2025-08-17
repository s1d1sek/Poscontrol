package com.ordersystem.backend.model;

import jakarta.persistence.*;  // Import JPA annotations for database mapping
import java.math.BigDecimal;   // For precise decimal calculations (prices)

// @Entity tells Spring this class represents a database table
@Entity
// @Table specifies the actual table name in the database
@Table(name = "products")
public class Product {
    
    // @Id marks this field as the primary key
    @Id
    // @GeneratedValue means the database will auto-generate this value
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    // @Column(nullable = false) means this field cannot be empty in the database
    @Column(nullable = false)
    private String name;
    
    // Description is optional (nullable = true by default)
    private String description;
    
    // Use BigDecimal for money to avoid floating point precision issues
    @Column(nullable = false)
    private BigDecimal price;
    
    // How many items we currently have in stock
    @Column(nullable = false)
    private Integer stockQuantity;
    
    // Minimum stock level - when to reorder
    @Column(nullable = false)
    private Integer minStockLevel;
    
    // Default constructor (required by JPA)
    public Product() {}
    
    // Constructor with parameters for easy object creation
    public Product(String name, String description, BigDecimal price, 
                  Integer stockQuantity, Integer minStockLevel) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.stockQuantity = stockQuantity;
        this.minStockLevel = minStockLevel;
    }
    
    // Getter and Setter methods (required for JPA and Spring)
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public BigDecimal getPrice() {
        return price;
    }
    
    public void setPrice(BigDecimal price) {
        this.price = price;
    }
    
    public Integer getStockQuantity() {
        return stockQuantity;
    }
    
    public void setStockQuantity(Integer stockQuantity) {
        this.stockQuantity = stockQuantity;
    }
    
    public Integer getMinStockLevel() {
        return minStockLevel;
    }
    
    public void setMinStockLevel(Integer minStockLevel) {
        this.minStockLevel = minStockLevel;
    }
    
    // toString method for debugging and logging
    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", stockQuantity=" + stockQuantity +
                '}';
    }
}