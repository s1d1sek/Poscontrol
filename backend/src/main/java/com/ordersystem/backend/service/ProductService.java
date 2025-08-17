package com.ordersystem.backend.service;

import com.ordersystem.backend.model.Product;
import com.ordersystem.backend.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

// @Service tells Spring this is a service component (business logic layer)
@Service
// @Transactional ensures database operations are atomic (all succeed or all fail)
@Transactional
public class ProductService {
    
    // @Autowired injects the ProductRepository dependency
    @Autowired
    private ProductRepository productRepository;
    
    // Get all products
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }
    
    // Get a single product by ID
    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }
    
    // Create a new product
    public Product createProduct(Product product) {
        // Validate product data before saving
        validateProduct(product);
        return productRepository.save(product);
    }
    
    // Update an existing product
    public Product updateProduct(Long id, Product productDetails) {
        Product product = productRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
        
        // Update product fields
        product.setName(productDetails.getName());
        product.setDescription(productDetails.getDescription());
        product.setPrice(productDetails.getPrice());
        product.setStockQuantity(productDetails.getStockQuantity());
        product.setMinStockLevel(productDetails.getMinStockLevel());
        
        return productRepository.save(product);
    }
    
    // Delete a product
    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
        productRepository.delete(product);
    }
    
    // Get products with low stock
    public List<Product> getLowStockProducts() {
        return productRepository.findLowStockProducts();
    }
    
    // Get products that need restocking
    public List<Product> getProductsNeedingRestock() {
        return productRepository.findProductsNeedingRestock();
    }
    
    // Search products by name
    public List<Product> searchProductsByName(String name) {
        return productRepository.findByNameContainingIgnoreCase(name);
    }
    
    // Get products in stock
    public List<Product> getProductsInStock() {
        return productRepository.findByStockQuantityGreaterThan(0);
    }
    
    // Update stock quantity when an order is placed
    public void updateStock(Long productId, Integer quantityToDeduct) {
        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new RuntimeException("Product not found with id: " + productId));
        
        // Check if enough stock is available
        if (product.getStockQuantity() < quantityToDeduct) {
            throw new RuntimeException("Insufficient stock for product: " + product.getName() + 
                ". Available: " + product.getStockQuantity() + ", Requested: " + quantityToDeduct);
        }
        
        // Deduct the quantity
        product.setStockQuantity(product.getStockQuantity() - quantityToDeduct);
        productRepository.save(product);
        
        // Check if stock is below minimum level and log warning
        if (product.getStockQuantity() <= product.getMinStockLevel()) {
            System.out.println("WARNING: Product " + product.getName() + 
                " is below minimum stock level. Current: " + product.getStockQuantity() + 
                ", Minimum: " + product.getMinStockLevel());
        }
    }
    
    // Restock a product (add to existing stock)
    public Product restockProduct(Long productId, Integer quantityToAdd) {
        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new RuntimeException("Product not found with id: " + productId));
        
        product.setStockQuantity(product.getStockQuantity() + quantityToAdd);
        return productRepository.save(product);
    }
    
    // Check if product is available in required quantity
    public boolean isProductAvailable(Long productId, Integer requiredQuantity) {
        Optional<Product> product = productRepository.findById(productId);
        return product.isPresent() && product.get().getStockQuantity() >= requiredQuantity;
    }
    
    // Validate product data
    private void validateProduct(Product product) {
        if (product.getName() == null || product.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Product name cannot be empty");
        }
        
        if (product.getPrice() == null || product.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Product price must be greater than zero");
        }
        
        if (product.getStockQuantity() == null || product.getStockQuantity() < 0) {
            throw new IllegalArgumentException("Stock quantity cannot be negative");
        }
        
        if (product.getMinStockLevel() == null || product.getMinStockLevel() < 0) {
            throw new IllegalArgumentException("Minimum stock level cannot be negative");
        }
    }
    
    // Get product statistics
    public ProductStatistics getProductStatistics() {
        List<Product> allProducts = productRepository.findAll();
        List<Product> lowStockProducts = productRepository.findLowStockProducts();
        
        ProductStatistics stats = new ProductStatistics();
        stats.setTotalProducts(allProducts.size());
        stats.setLowStockCount(lowStockProducts.size());
        stats.setOutOfStockCount(
            allProducts.stream()
                .filter(p -> p.getStockQuantity() == 0)
                .count()
        );
        
        return stats;
    }
    
    // Inner class for product statistics
    public static class ProductStatistics {
        private int totalProducts;
        private int lowStockCount;
        private long outOfStockCount;
        
        // Getters and setters
        public int getTotalProducts() {
            return totalProducts;
        }
        
        public void setTotalProducts(int totalProducts) {
            this.totalProducts = totalProducts;
        }
        
        public int getLowStockCount() {
            return lowStockCount;
        }
        
        public void setLowStockCount(int lowStockCount) {
            this.lowStockCount = lowStockCount;
        }
        
        public long getOutOfStockCount() {
            return outOfStockCount;
        }
        
        public void setOutOfStockCount(long outOfStockCount) {
            this.outOfStockCount = outOfStockCount;
        }
    }
}