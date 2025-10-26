package org.product_service.controller;

import jakarta.validation.Valid;
import org.product_service.entity.Product;
import org.product_service.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductService service;

    @PostMapping
    public ResponseEntity<?> addProduct(@Valid @RequestBody Product product) {
        try {
            Product saved = service.addProduct(product);
            return ResponseEntity.status(HttpStatus.CREATED).body(saved);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @PostMapping("/multiple")
    public ResponseEntity<?> addMultipleProduct(@Valid @RequestBody List<Product> products) {
        try {
            List<Product> saved = service.addMultipleProducts(products);
            return ResponseEntity.status(HttpStatus.CREATED).body(saved);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts(@RequestParam(name = "offset", defaultValue = "0", required = false) Integer offset,
                                                        @RequestParam(name = "noOfRecords", defaultValue = "0", required = false) Integer noOfRecords) {
        return ResponseEntity.ok(service.getAllProducts(offset, noOfRecords));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getProductById(@PathVariable UUID id) {
        try {
            return ResponseEntity.ok(service.getProductById(id));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Product Not found");
        }
    }

    @PutMapping
    public ResponseEntity<?> updateProduct(@Valid @RequestBody Product product) {
        try {
            return ResponseEntity.ok(service.updateProduct(product));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Product Not found to update");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable UUID id) {
        return ResponseEntity.ok(service.deleteProduct(id));
    }
}

