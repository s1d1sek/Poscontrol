package com.ordersystem.backend.repository;

import com.ordersystem.backend.model.Order;
import com.ordersystem.backend.model.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
// JpaRepository provides: save(), findAll(), findById(), deleteById(), etc.
public interface OrderRepository extends JpaRepository<Order, Long> {
    
    // Find orders within a date range
    // This is useful for daily/monthly reports
    List<Order> findByOrderDateBetween(LocalDateTime startDate, LocalDateTime endDate);
    
    // Find all orders for a specific customer (case-insensitive)
    List<Order> findByCustomerEmailIgnoreCase(String email);
    
    // Find orders by status
    List<Order> findByStatus(OrderStatus status);
    
    // Find orders by customer name (partial match, case-insensitive)
    List<Order> findByCustomerNameContainingIgnoreCase(String name);
    
    // Custom query to find today's orders
    @Query("SELECT o FROM Order o WHERE DATE(o.orderDate) = CURRENT_DATE ORDER BY o.orderDate DESC")
    List<Order> findTodaysOrders();
    
    // Find orders with total amount greater than specified value
    List<Order> findByTotalAmountGreaterThan(java.math.BigDecimal amount);
    
    // Custom query to get order statistics
    @Query("SELECT COUNT(o) FROM Order o WHERE o.status = :status")
    Long countOrdersByStatus(@Param("status") OrderStatus status);
    
    // Find recent orders (last N orders)
    @Query("SELECT o FROM Order o ORDER BY o.orderDate DESC")
    List<Order> findRecentOrders();
    
    // Find orders by date range and status
    List<Order> findByOrderDateBetweenAndStatus(LocalDateTime startDate, LocalDateTime endDate, OrderStatus status);
}