package org.order_service.service;

import org.order_service.dto.OrderCreatedEvent;
import org.order_service.dto.OrderRequest;
import org.order_service.entity.Orders;
import org.order_service.kafka.OrderEventProducer;
import org.order_service.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Date;
import java.util.List;
import java.util.UUID;

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
        var product = restTemplate.getForObject(url, java.util.Map.class);

        if (product == null) {
            throw new RuntimeException("Product not found");
        }

        int stock = (Integer) product.get("stock");
        if (stock < request.getQuantity()) {
            throw new RuntimeException("Insufficient stock");
        }

        product.put("stock", stock - request.getQuantity());
        restTemplate.put(productServiceUrl, product);

        Orders order = new Orders();
        order.setId(UUID.randomUUID());
        order.setProductId(request.getProductId());
        order.setProductName((String) product.get("name"));
        order.setUnitPrice(Double.parseDouble(product.get("price").toString()));
        order.setQuantity(request.getQuantity());
        order.setTotalPrice(order.getUnitPrice() * order.getQuantity());
        order.setCustomerId(request.getCustomerId());
        order.setCreatedAt(new Date());

        Orders savedOrder = orderRepository.save(order);

        OrderCreatedEvent event = new OrderCreatedEvent(
                savedOrder.getId(),
                savedOrder.getProductId(),
                savedOrder.getProductName(),
                savedOrder.getQuantity(),
                savedOrder.getTotalPrice(),
                savedOrder.getCustomerId(),
                savedOrder.getCreatedAt()
        );
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
