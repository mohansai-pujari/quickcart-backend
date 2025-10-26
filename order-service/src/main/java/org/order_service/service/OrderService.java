package org.order_service.service;

import org.order_service.dto.OrderCreatedEvent;
import org.order_service.dto.OrderRequest;
import org.order_service.entity.Orders;
import org.order_service.kafka.OrderEventProducer;
import org.order_service.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderEventProducer producer;

    private final RestTemplate restTemplate = new RestTemplate();

    private final String productServiceUrl = "http://localhost:8081/api/products";

    public Orders createOrder(OrderRequest request) {
        String url = productServiceUrl + "/" + request.getProductId();
        Map product = restTemplate.getForObject(url, Map.class);

        if (product == null) {
            throw new RuntimeException("Product not found");
        }

        int stock = (Integer) product.get("stock");
        if (stock < request.getQuantity()) {
            throw new RuntimeException("Insufficient stock");
        }

        product.put("stock", stock - request.getQuantity());
        restTemplate.put(productServiceUrl, product);

        Orders order = new Orders(product, request);

        Orders savedOrder = orderRepository.save(order);

        OrderCreatedEvent event = new OrderCreatedEvent(savedOrder);
        producer.publishOrderCreated(event);

        return savedOrder;
    }

    public Orders getOrder(UUID id) {
        return orderRepository.findById(id).orElse(null);
    }

    public List<Orders> getAllOrders() {
        return orderRepository.findAll();
    }

    public List<Orders> getOrdersByCustomer(String customerId) {
        return orderRepository.findByCustomerId(customerId);
    }
}
