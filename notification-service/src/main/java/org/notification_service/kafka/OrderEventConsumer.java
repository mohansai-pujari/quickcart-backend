package org.notification_service.kafka;

import org.notification_service.dto.OrderCreatedEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderEventConsumer {

    private final List<OrderCreatedEvent> notifications = new ArrayList<>();

    @KafkaListener(topics = "${spring.kafka.topic.order-events}", groupId = "${spring.kafka.consumer.group-id}")
    public void consumeOrderEvent(OrderCreatedEvent event) {
        try {
            System.out.println("[Notification Service] Received OrderCreated event: " + event);
            notifications.add(event);
        } catch (Exception e) {
            System.err.println("[Notification Service] Ignored exception: " + e.getMessage());
        }
    }

    public List<OrderCreatedEvent> getNotifications() {
        return notifications;
    }
}

