package com.ordersystem.backend.controller;

// Import all necessary classes
import com.ordersystem.backend.dto.OrderRequest;
import com.ordersystem.backend.model.Order;
import com.ordersystem.backend.model.OrderStatus;
import com.ordersystem.backend.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

// @RestController tells Spring this class handles REST API requests
@RestController
// Base URL for all order-related endpoints
@RequestMapping("/api/orders")
// Allow cross-origin requests from Angular frontend
@CrossOrigin(origins = "http://localhost:4200")
public class OrderController {
    
    // Inject the OrderService to handle business logic
    @Autowired
    private OrderService orderService;
    
    // POST endpoint: http://localhost:8080/api/orders
    // Creates a new order from the request data
    @PostMapping
    public ResponseEntity<?> createOrder(@RequestBody OrderRequest orderRequest) {
        try {
            // The OrderRequest DTO contains customer info and order items
            // The service will validate stock, create the order, and update inventory
            Order createdOrder = orderService.createOrder(orderRequest);
            
            // Return the created order with 201 CREATED status
            return ResponseEntity.status(HttpStatus.CREATED).body(createdOrder);
            
        } catch (RuntimeException e) {
            // If something goes wrong (e.g., insufficient stock), return error message
            // Creating a simple error response object
            return ResponseEntity.badRequest().body(
                new ErrorResponse(e.getMessage())
            );
        }
    }
    
    // GET endpoint: http://localhost:8080/api/orders
    // Returns all orders in the system
    @GetMapping
    public List<Order> getAllOrders() {
        return orderService.getAllOrders();
    }
    
    // GET endpoint: http://localhost:8080/api/orders/{id}
    // Returns a specific order by its ID
    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrderById(@PathVariable Long id) {
        // Try to find the order
        Optional<Order> order = orderService.getOrderById(id);
        
        // Return the order if found, or 404 if not found
        if (order.isPresent()) {
            return ResponseEntity.ok(order.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    // GET endpoint: http://localhost:8080/api/orders/status/{status}
    // Returns all orders with a specific status (PENDING, COMPLETED, CANCELLED)
    // Example: /api/orders/status/PENDING
    @GetMapping("/status/{status}")
    public ResponseEntity<List<Order>> getOrdersByStatus(@PathVariable String status) {
        try {
            // Convert string to OrderStatus enum
            // valueOf throws IllegalArgumentException if status is invalid
            OrderStatus orderStatus = OrderStatus.valueOf(status.toUpperCase());
            List<Order> orders = orderService.getOrdersByStatus(orderStatus);
            return ResponseEntity.ok(orders);
        } catch (IllegalArgumentException e) {
            // If invalid status provided, return 400 BAD REQUEST
            return ResponseEntity.badRequest().build();
        }
    }
    
    // GET endpoint: http://localhost:8080/api/orders/customer?email=customer@example.com
    // Find all orders for a specific customer by email
    @GetMapping("/customer")
    public List<Order> getOrdersByCustomerEmail(@RequestParam String email) {
        return orderService.getOrdersByCustomerEmail(email);
    }
    
    // GET endpoint: http://localhost:8080/api/orders/today
    // Returns all orders placed today
    @GetMapping("/today")
    public List<Order> getTodaysOrders() {
        return orderService.getTodaysOrders();
    }
    
    // PUT endpoint: http://localhost:8080/api/orders/{id}/status
// Updates the status of an order
// Request body should contain: {"status": "COMPLETED"}
@PutMapping("/{id}/status")
public ResponseEntity<?> updateOrderStatus(
        @PathVariable Long id,
        @RequestBody StatusUpdateRequest statusRequest) {
    try {
        // Parse the new status from the request
        OrderStatus newStatus = OrderStatus.valueOf(statusRequest.getStatus().toUpperCase());
        
        // Update the order status
        // This may also restore stock if order is cancelled
        Order updatedOrder = orderService.updateOrderStatus(id, newStatus);
        
        return ResponseEntity.ok(updatedOrder);
    } catch (IllegalArgumentException e) {
        // Invalid status provided - catch this FIRST (more specific exception)
        return ResponseEntity.badRequest().body(
            new ErrorResponse("Invalid status: " + statusRequest.getStatus())
        );
    } catch (RuntimeException e) {
        // Order not found - catch this SECOND (more general exception)
        // This will catch any other RuntimeException that isn't IllegalArgumentException
        return ResponseEntity.notFound().build();
    }
}
    
    // PUT endpoint: http://localhost:8080/api/orders/{id}/cancel
    // Cancels an order and restores the stock
    @PutMapping("/{id}/cancel")
    public ResponseEntity<?> cancelOrder(@PathVariable Long id) {
        try {
            // Cancel the order (this will restore stock automatically)
            Order cancelledOrder = orderService.cancelOrder(id);
            return ResponseEntity.ok(cancelledOrder);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    // PUT endpoint: http://localhost:8080/api/orders/{id}/complete
    // Marks an order as completed
    @PutMapping("/{id}/complete")
    public ResponseEntity<?> completeOrder(@PathVariable Long id) {
        try {
            // Mark the order as completed
            Order completedOrder = orderService.completeOrder(id);
            return ResponseEntity.ok(completedOrder);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    // GET endpoint: http://localhost:8080/api/orders/statistics
    // Returns statistics about all orders
    @GetMapping("/statistics")
    public OrderService.OrderStatistics getOrderStatistics() {
        // Returns counts of orders by status and total revenue
        return orderService.getOrderStatistics();
    }
    
    // Inner class for error responses
    // This creates a JSON response like: {"error": "Error message here"}
    static class ErrorResponse {
        private String error;
        
        public ErrorResponse(String error) {
            this.error = error;
        }
        
        public String getError() {
            return error;
        }
        
        public void setError(String error) {
            this.error = error;
        }
    }
    
    // Inner class for status update requests
    // This handles JSON like: {"status": "COMPLETED"}
    static class StatusUpdateRequest {
        private String status;
        
        public String getStatus() {
            return status;
        }
        
        public void setStatus(String status) {
            this.status = status;
        }
    }
}