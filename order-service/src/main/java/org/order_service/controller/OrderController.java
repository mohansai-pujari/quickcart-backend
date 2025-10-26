package org.order_service.controller;

import jakarta.validation.Valid;
import org.order_service.dto.OrderRequest;
import org.order_service.entity.Orders;
import org.order_service.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService service;

    @PostMapping
    public ResponseEntity<?> createOrder(@Valid @RequestBody OrderRequest request) {
        try {
            Orders saved = service.createOrder(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(saved);
        } catch (RuntimeException re) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(re.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getOrder(@PathVariable UUID id) {
        Orders orders = service.getOrder(id);
        if(orders == null){
            return ResponseEntity.ok("No order found with the given id");
        }
        return ResponseEntity.ok(orders);
    }

    @GetMapping
    public ResponseEntity<List<Orders>> getAllOrders() {
        return ResponseEntity.ok(service.getAllOrders());
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<Orders>> getOrdersByCustomer(@PathVariable String customerId) {
        return ResponseEntity.ok(service.getOrdersByCustomer(customerId));
    }
}
