package com.ordersystem.backend.service;

import com.ordersystem.backend.dto.OrderRequest;
import com.ordersystem.backend.dto.OrderItemRequest;
import com.ordersystem.backend.model.*;
import com.ordersystem.backend.repository.OrderRepository;
import com.ordersystem.backend.repository.OrderItemRepository;
import com.ordersystem.backend.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class OrderService {
    
    @Autowired
    private OrderRepository orderRepository;
    
    @Autowired
    private ProductRepository productRepository;
    
    @Autowired
    private OrderItemRepository orderItemRepository;
    
    // Create a new order
    public Order createOrder(OrderRequest orderRequest) {
        // First, validate that all products have sufficient stock
        validateStockAvailability(orderRequest.getOrderItems());
        
        // Create new order
        Order order = new Order();
        order.setCustomerName(orderRequest.getCustomerName());
        order.setCustomerEmail(orderRequest.getCustomerEmail());
        order.setOrderDate(LocalDateTime.now());
        order.setStatus(OrderStatus.PENDING);
        
        List<OrderItem> orderItems = new ArrayList<>();
        BigDecimal totalAmount = BigDecimal.ZERO;
        
        // Process each order item
        for (OrderItemRequest itemRequest : orderRequest.getOrderItems()) {
            Product product = productRepository.findById(itemRequest.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + itemRequest.getProductId()));
            
            // Create order item
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(product);
            orderItem.setQuantity(itemRequest.getQuantity());
            orderItem.setUnitPrice(product.getPrice());
            
            orderItems.add(orderItem);
            
            // Calculate total amount
            BigDecimal itemTotal = product.getPrice().multiply(BigDecimal.valueOf(itemRequest.getQuantity()));
            totalAmount = totalAmount.add(itemTotal);
            
            // Update product stock
            product.setStockQuantity(product.getStockQuantity() - itemRequest.getQuantity());
            productRepository.save(product);
            
            // Check if product needs restocking
            if (product.getStockQuantity() <= product.getMinStockLevel()) {
                System.out.println("WARNING: Product " + product.getName() + 
                    " has reached minimum stock level. Current stock: " + product.getStockQuantity());
            }
        }
        
        order.setOrderItems(orderItems);
        order.setTotalAmount(totalAmount);
        
        // Save the order (cascade will save order items)
        return orderRepository.save(order);
    }
    
    // Add this method to OrderService if you want to use the OrderItemRepository
    public List<OrderItem> getOrderItems(Long orderId) {
        return orderItemRepository.findByOrderId(orderId);
    }

    // Or this method to get popular products
    public List<Object[]> getMostPopularProducts() {
        return orderItemRepository.findMostPopularProducts();
}
    // Validate that all products have sufficient stock
    private void validateStockAvailability(List<OrderItemRequest> orderItems) {
        for (OrderItemRequest item : orderItems) {
            Product product = productRepository.findById(item.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + item.getProductId()));
            
            if (product.getStockQuantity() < item.getQuantity()) {
                throw new RuntimeException(
                    "Insufficient stock for product: " + product.getName() + 
                    ". Available: " + product.getStockQuantity() + 
                    ", Requested: " + item.getQuantity()
                );
            }
        }
    }
    
    // Get all orders
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }
    
    // Get order by ID
    public Optional<Order> getOrderById(Long id) {
        return orderRepository.findById(id);
    }
    
    // Get orders by status
    public List<Order> getOrdersByStatus(OrderStatus status) {
        return orderRepository.findByStatus(status);
    }
    
    // Get orders by customer email
    public List<Order> getOrdersByCustomerEmail(String email) {
        return orderRepository.findByCustomerEmailIgnoreCase(email);
    }
    
    // Get today's orders
    public List<Order> getTodaysOrders() {
        return orderRepository.findTodaysOrders();
    }
    
    // Update order status
    public Order updateOrderStatus(Long orderId, OrderStatus newStatus) {
        Order order = orderRepository.findById(orderId)
            .orElseThrow(() -> new RuntimeException("Order not found with id: " + orderId));
        
        OrderStatus oldStatus = order.getStatus();
        order.setStatus(newStatus);
        
        // If order is cancelled, restore the stock
        if (newStatus == OrderStatus.CANCELLED && oldStatus != OrderStatus.CANCELLED) {
            restoreStock(order);
        }
        
        return orderRepository.save(order);
    }
    
    // Restore stock when order is cancelled
    private void restoreStock(Order order) {
        for (OrderItem item : order.getOrderItems()) {
            Product product = item.getProduct();
            product.setStockQuantity(product.getStockQuantity() + item.getQuantity());
            productRepository.save(product);
            System.out.println("Stock restored for product: " + product.getName() + 
                ", Quantity: " + item.getQuantity());
        }
    }
    
    // Cancel an order
    public Order cancelOrder(Long orderId) {
        return updateOrderStatus(orderId, OrderStatus.CANCELLED);
    }
    
    // Complete an order
    public Order completeOrder(Long orderId) {
        return updateOrderStatus(orderId, OrderStatus.COMPLETED);
    }
    
    // Get order statistics
    public OrderStatistics getOrderStatistics() {
        OrderStatistics stats = new OrderStatistics();
        stats.setTotalOrders(orderRepository.count());
        stats.setPendingOrders(orderRepository.countOrdersByStatus(OrderStatus.PENDING));
        stats.setCompletedOrders(orderRepository.countOrdersByStatus(OrderStatus.COMPLETED));
        stats.setCancelledOrders(orderRepository.countOrdersByStatus(OrderStatus.CANCELLED));
        
        // Calculate total revenue from completed orders
        List<Order> completedOrders = orderRepository.findByStatus(OrderStatus.COMPLETED);
        BigDecimal totalRevenue = completedOrders.stream()
            .map(Order::getTotalAmount)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        stats.setTotalRevenue(totalRevenue);
        
        return stats;
    }
    
    // Inner class for order statistics
    public static class OrderStatistics {
        private Long totalOrders;
        private Long pendingOrders;
        private Long completedOrders;
        private Long cancelledOrders;
        private BigDecimal totalRevenue;
        
        // Getters and setters
        public Long getTotalOrders() {
            return totalOrders;
        }
        
        public void setTotalOrders(Long totalOrders) {
            this.totalOrders = totalOrders;
        }
        
        public Long getPendingOrders() {
            return pendingOrders;
        }
        
        public void setPendingOrders(Long pendingOrders) {
            this.pendingOrders = pendingOrders;
        }
        
        public Long getCompletedOrders() {
            return completedOrders;
        }
        
        public void setCompletedOrders(Long completedOrders) {
            this.completedOrders = completedOrders;
        }
        
        public Long getCancelledOrders() {
            return cancelledOrders;
        }
        
        public void setCancelledOrders(Long cancelledOrders) {
            this.cancelledOrders = cancelledOrders;
        }
        
        public BigDecimal getTotalRevenue() {
            return totalRevenue;
        }
        
        public void setTotalRevenue(BigDecimal totalRevenue) {
            this.totalRevenue = totalRevenue;
        }
    }
}