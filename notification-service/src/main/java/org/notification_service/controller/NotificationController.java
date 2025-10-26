package org.notification_service.controller;

import org.notification_service.dto.OrderCreatedEvent;
import org.notification_service.kafka.OrderEventConsumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    @Autowired
    private OrderEventConsumer service;


    @GetMapping
    public ResponseEntity<List<OrderCreatedEvent>> getNotifications() {
        return ResponseEntity.ok(service.getNotifications());
    }

}