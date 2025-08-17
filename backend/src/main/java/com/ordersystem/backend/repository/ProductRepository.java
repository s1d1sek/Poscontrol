package com.ordersystem.backend.repository;

// Import necessary JPA and Spring Data classes
import com.ordersystem.backend.model.Product;  // Our Product entity
import org.springframework.data.jpa.repository.JpaRepository;  // Spring Data JPA interface
import org.springframework.data.jpa.repository.Query;  // For custom queries
import org.springframework.stereotype.Repository;  // Spring annotation
import java.util.List;

// @Repository tells Spring this is a data access component
@Repository
// JpaRepository<Product, Long> means:
// - We're working with Product entities
// - The primary key type is Long
// This interface provides basic CRUD operations automatically
public interface ProductRepository extends JpaRepository<Product, Long> {
    
    // Custom query method: Spring automatically generates the SQL
    // Method name follows Spring Data naming convention:
    // findBy + FieldName + ComparisonOperation
    List<Product> findByStockQuantityLessThan(Integer threshold);
    
    // Another way to write the low stock query using a custom query
    // @Query annotation allows us to write our own SQL/JPQL
    @Query("SELECT p FROM Product p WHERE p.stockQuantity < p.minStockLevel")
    List<Product> findLowStockProducts();
    
    // Find products by name (case-insensitive search)
    // "Containing" means it will search for partial matches
    // "IgnoreCase" makes it case-insensitive
    List<Product> findByNameContainingIgnoreCase(String name);
    
    // Find products by exact name
    Product findByName(String name);
    
    // Find products within a price range
    List<Product> findByPriceBetween(java.math.BigDecimal minPrice, java.math.BigDecimal maxPrice);
    
    // Find products that are in stock (quantity > 0)
    List<Product> findByStockQuantityGreaterThan(Integer quantity);
    
    // Custom query to find products that need restocking
    @Query("SELECT p FROM Product p WHERE p.stockQuantity <= p.minStockLevel ORDER BY p.stockQuantity ASC")
    List<Product> findProductsNeedingRestock();
}