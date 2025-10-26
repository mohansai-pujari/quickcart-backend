package org.notification_service;

import org.junit.jupiter.api.Test;
import org.notification_service.dto.OrderCreatedEvent;
import org.notification_service.kafka.OrderEventConsumer;

import java.util.Date;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class NotificationConsumerTest {

    private static final UUID FIXED_ORDER_ID = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
    private static final UUID FIXED_PRODUCT_ID = UUID.fromString("987e6543-e21b-43d3-b456-123456789000");

    @Test
    void testConsumeOrderEvent() {
        OrderEventConsumer consumer = new OrderEventConsumer();

        OrderCreatedEvent event = new OrderCreatedEvent(FIXED_ORDER_ID, FIXED_PRODUCT_ID, "Wireless Mouse",
                1, 30000.0, "CUST-50000", new Date());

        consumer.consumeOrderEvent(event);

        assertEquals(1, consumer.getNotifications().size());
        OrderCreatedEvent stored = consumer.getNotifications().get(0);

        assertEquals(FIXED_ORDER_ID, stored.getOrderId());
        assertEquals(FIXED_PRODUCT_ID, stored.getProductId());
        assertEquals("Wireless Mouse", stored.getProductName());
        assertEquals(1, stored.getQuantity());
        assertEquals(30000.0, stored.getTotalPrice());
    }
}
