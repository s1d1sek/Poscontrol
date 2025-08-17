package com.ordersystem.backend.repository;

import com.ordersystem.backend.model.OrderItem;
import com.ordersystem.backend.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    
    // Find all order items for a specific order
    List<OrderItem> findByOrderId(Long orderId);
    
    // Find all order items for a specific product
    // This is useful to see how popular a product is
    List<OrderItem> findByProduct(Product product);
    
    // Find all order items for a specific product by ID
    List<OrderItem> findByProductId(Long productId);
    
    // Custom query to find the most popular products
    // This groups order items by product and sums the quantities
    @Query("SELECT oi.product, SUM(oi.quantity) as totalSold " +
           "FROM OrderItem oi " +
           "GROUP BY oi.product " +
           "ORDER BY totalSold DESC")
    List<Object[]> findMostPopularProducts();
    
    // Find order items with quantity greater than specified amount
    List<OrderItem> findByQuantityGreaterThan(Integer quantity);
    
    // Custom query to get total revenue for a specific product
    @Query("SELECT SUM(oi.unitPrice * oi.quantity) FROM OrderItem oi WHERE oi.product.id = :productId")
    java.math.BigDecimal getTotalRevenueForProduct(@Param("productId") Long productId);
    
    // Custom query to find all items in pending orders
    @Query("SELECT oi FROM OrderItem oi WHERE oi.order.status = 'PENDING'")
    List<OrderItem> findItemsInPendingOrders();
}