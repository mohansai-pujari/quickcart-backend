package org.order_service.dto;

import org.order_service.entity.Orders;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

public class OrderCreatedEvent implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private UUID orderId;
    private UUID productId;
    private String productName;
    private int quantity;
    private double totalPrice;
    private String customerId;
    private Date createdAt;

    public OrderCreatedEvent() {
    }

    public OrderCreatedEvent(UUID orderId, UUID productId, String productName, int quantity,
                             double totalPrice, String customerId, Date createdAt) {
        this.orderId = orderId;
        this.productId = productId;
        this.productName = productName;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
        this.customerId = customerId;
        this.createdAt = createdAt;
    }

    public OrderCreatedEvent(Orders orders){
        this.orderId = orders.getId();
        this.productId = orders.getProductId();
        this.productName = orders.getProductName();
        this.quantity = orders.getQuantity();
        this.totalPrice = orders.getTotalPrice();
        this.customerId = orders.getCustomerId();
        this.createdAt = orders.getCreatedAt();
    }

    public UUID getOrderId() {
        return orderId;
    }

    public void setOrderId(UUID orderId) {
        this.orderId = orderId;
    }

    public UUID getProductId() {
        return productId;
    }

    public void setProductId(UUID productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "OrderCreatedEvent{" +
                "orderId=" + orderId +
                ", productId=" + productId +
                ", productName='" + productName + '\'' +
                ", quantity=" + quantity +
                ", totalPrice=" + totalPrice +
                ", customerId='" + customerId + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}
