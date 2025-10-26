package org.order_service.entity;

import jakarta.persistence.*;
import org.order_service.dto.OrderRequest;

import java.util.*;

@Entity
public class Orders {

    @Id
    @GeneratedValue
    private UUID id;

    private UUID productId;
    private String productName;
    private double unitPrice;
    private int quantity;
    private double totalPrice;
    private String customerId;
    private Date createdAt;

    public Orders() {
    }

    public Orders(Map product, OrderRequest request) {
        this.id = UUID.randomUUID();
        this.productId = request.getProductId();
        this.customerId = request.getCustomerId();
        this.productName = (String) product.get("name");
        this.quantity = request.getQuantity();
        this.unitPrice = Double.parseDouble(product.get("price").toString());
        this.totalPrice = unitPrice * quantity;
        this.createdAt = new Date(System.currentTimeMillis());
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
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

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
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
}