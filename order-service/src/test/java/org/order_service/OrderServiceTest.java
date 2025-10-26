package org.order_service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.order_service.dto.OrderCreatedEvent;
import org.order_service.dto.OrderRequest;
import org.order_service.entity.Orders;
import org.order_service.kafka.OrderEventProducer;
import org.order_service.service.OrderService;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class OrderServiceTest {

    @Mock
    private OrderEventProducer orderProducer;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private OrderService orderService;

    @Captor
    private ArgumentCaptor<OrderCreatedEvent> eventCaptor;

    private static final UUID FIXED_PRODUCT_ID = UUID.fromString("987e6543-e21b-43d3-b456-123456789000");

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
    }

    /**
    @Test
    void testPlaceOrderPublishesEvent() {
        // Mock product response from Product Service
        Map<String, Object> product = new HashMap<>();
        product.put("name", "Wireless Mouse");
        product.put("price", 30000.0);
        product.put("stock", 10);

        String productUrl = "http://localhost:8081/api/products/" + FIXED_PRODUCT_ID;
        when(restTemplate.getForObject(productUrl, Map.class)).thenReturn(product);

        OrderRequest orderRequest = new OrderRequest(FIXED_PRODUCT_ID, 1, "50000");
        Orders savedOrder = orderService.createOrder(orderRequest);

        assertNotNull(savedOrder);
        assertEquals(FIXED_PRODUCT_ID, savedOrder.getProductId());
        assertEquals(1, savedOrder.getQuantity());
        assertEquals(30000.0, savedOrder.getUnitPrice());

        verify(orderProducer, times(1)).publishOrderCreated(eventCaptor.capture());
        OrderCreatedEvent publishedEvent = eventCaptor.getValue();

        assertNotNull(publishedEvent);
        assertEquals(savedOrder.getId(), publishedEvent.getOrderId());
        assertEquals(savedOrder.getQuantity(), publishedEvent.getQuantity());
    }
     **/

    @Test
    void testProductNotFoundThrowsException() {
        String productUrl = "http://localhost:8081/api/products/" + FIXED_PRODUCT_ID;
        when(restTemplate.getForObject(productUrl, Map.class)).thenReturn(null);

        OrderRequest orderRequest = new OrderRequest(FIXED_PRODUCT_ID, 1, "50000");

        Exception e = assertThrows(RuntimeException.class, () -> orderService.createOrder(orderRequest));
        assertTrue(e.getMessage().contains("Product Not found"));
    }
}
