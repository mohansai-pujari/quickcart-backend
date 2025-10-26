package org.order_service.kafka;

import org.order_service.dto.OrderCreatedEvent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class OrderEventProducer {

    private final KafkaTemplate<String, OrderCreatedEvent> kafkaTemplate;
    private final String topic;

    public OrderEventProducer(KafkaTemplate<String, OrderCreatedEvent> kafkaTemplate,
                              @Value("${spring.kafka.topic.order-events}") String topic) {
        this.kafkaTemplate = kafkaTemplate;
        this.topic = topic;
    }

    public void publishOrderCreated(OrderCreatedEvent event) {
        kafkaTemplate.send(topic, event);
        System.out.println("[Kafka Producer] Published OrderCreated event: " + event);
    }
}
