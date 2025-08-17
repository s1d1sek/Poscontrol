package com.ordersystem.backend.controller;

// All necessary imports for ProductController
import com.ordersystem.backend.model.Product;
import com.ordersystem.backend.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

// @RestController annotation and rest of your class...

// @RestController combines @Controller and @ResponseBody
// It tells Spring this class will handle HTTP requests and return JSON data
@RestController
// @RequestMapping defines the base URL path for all methods in this controller
// All endpoints in this class will start with "/api/products"
@RequestMapping("/api/products")
// @CrossOrigin allows the Angular frontend (running on port 4200) to access these endpoints
// Without this, the browser would block requests due to CORS policy
@CrossOrigin(origins = "http://localhost:4200")
public class ProductController {
    
    // @Autowired automatically injects the ProductService dependency
    // Spring creates and manages the ProductService instance for us
    @Autowired
    private ProductService productService;
    
    // GET endpoint: http://localhost:8080/api/products
    // @GetMapping handles HTTP GET requests
    // This method returns all products as a JSON array
    @GetMapping
    public List<Product> getAllProducts() {
        // Call the service layer to get all products from the database
        return productService.getAllProducts();
    }
    
    // GET endpoint: http://localhost:8080/api/products/{id}
    // {id} is a path variable - e.g., /api/products/1 gets product with ID 1
    // @PathVariable extracts the {id} value from the URL
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        // Optional is a container that may or may not contain a value
        // It helps avoid null pointer exceptions
        Optional<Product> product = productService.getProductById(id);
        
        // If product exists, return it with 200 OK status
        // If not found, return 404 NOT FOUND status
        if (product.isPresent()) {
            return ResponseEntity.ok(product.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    // POST endpoint: http://localhost:8080/api/products
    // @PostMapping handles HTTP POST requests (used for creating new resources)
    // @RequestBody converts the JSON request body into a Product object
    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestBody Product product) {
        try {
            // Save the new product using the service layer
            Product savedProduct = productService.createProduct(product);
            // Return the saved product with 201 CREATED status
            // CREATED is the standard status code for successful resource creation
            return ResponseEntity.status(HttpStatus.CREATED).body(savedProduct);
        } catch (IllegalArgumentException e) {
            // If validation fails, return 400 BAD REQUEST
            // This happens when product data is invalid (e.g., negative price)
            return ResponseEntity.badRequest().build();
        }
    }
    
    // PUT endpoint: http://localhost:8080/api/products/{id}
    // @PutMapping handles HTTP PUT requests (used for updating existing resources)
    // Updates the entire product with the given ID
    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(
            @PathVariable Long id,  // Get ID from URL
            @RequestBody Product productDetails) {  // Get updated data from request body
        try {
            // Update the product using the service layer
            Product updatedProduct = productService.updateProduct(id, productDetails);
            // Return the updated product with 200 OK status
            return ResponseEntity.ok(updatedProduct);
        } catch (RuntimeException e) {
            // If product not found, return 404 NOT FOUND
            return ResponseEntity.notFound().build();
        }
    }
    
    // DELETE endpoint: http://localhost:8080/api/products/{id}
    // @DeleteMapping handles HTTP DELETE requests
    // Deletes the product with the given ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        try {
            // Delete the product using the service layer
            productService.deleteProduct(id);
            // Return 204 NO CONTENT (successful deletion, no response body)
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            // If product not found, return 404 NOT FOUND
            return ResponseEntity.notFound().build();
        }
    }
    
    // GET endpoint: http://localhost:8080/api/products/low-stock
    // Custom endpoint to get products with low stock
    // The path is relative to the base path (/api/products)
    @GetMapping("/low-stock")
    public List<Product> getLowStockProducts() {
        // Returns all products where stock is below minimum level
        return productService.getLowStockProducts();
    }
    
    // GET endpoint: http://localhost:8080/api/products/search?name=productName
    // @RequestParam extracts query parameters from the URL
    // Example: /api/products/search?name=laptop
    @GetMapping("/search")
    public List<Product> searchProducts(@RequestParam String name) {
        // Search for products by name (partial match, case-insensitive)
        return productService.searchProductsByName(name);
    }
    
    // GET endpoint: http://localhost:8080/api/products/in-stock
    // Returns only products that have stock > 0
    @GetMapping("/in-stock")
    public List<Product> getProductsInStock() {
        return productService.getProductsInStock();
    }
    
    // PUT endpoint: http://localhost:8080/api/products/{id}/restock?quantity=50
    // Custom endpoint to add stock to a product
    // Combines path variable (id) and query parameter (quantity)
    @PutMapping("/{id}/restock")
    public ResponseEntity<Product> restockProduct(
            @PathVariable Long id,  // Product ID from URL
            @RequestParam Integer quantity) {  // Quantity to add from query parameter
        try {
            // Add the specified quantity to the product's stock
            Product restockedProduct = productService.restockProduct(id, quantity);
            return ResponseEntity.ok(restockedProduct);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    // GET endpoint: http://localhost:8080/api/products/statistics
    // Returns aggregated statistics about all products
    @GetMapping("/statistics")
    public ProductService.ProductStatistics getProductStatistics() {
        // Returns total products, low stock count, out of stock count
        return productService.getProductStatistics();
    }
}