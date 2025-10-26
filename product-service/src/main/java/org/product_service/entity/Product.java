package org.product_service.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Date;
import java.util.UUID;

@Entity
public class Product {

    @Id
    @GeneratedValue
    private UUID id;

    @NotBlank
    @Column(nullable = false, unique = true)
    private String name;

    @NotBlank
    private String sku;

    @Min(0)
    private double price;

    @Min(0)
    private int stock;

    private boolean activeStatus = true;

    private Date createdOn = new Date(System.currentTimeMillis());

    private Date updatedOn = new Date(System.currentTimeMillis());


    public Product() {
    }

    public Product(UUID id, String name, String sku, double price, int stock, boolean activeStatus) {
        this.id = id;
        this.name = name;
        this.sku = sku;
        this.price = price;
        this.stock = stock;
        this.activeStatus = activeStatus;
        this.createdOn = new Date(System.currentTimeMillis());;
        this.updatedOn = new Date(System.currentTimeMillis());;
    }

    public void productUpdate(Product productToUpdate) {
        this.name = productToUpdate.getName();
        this.sku = productToUpdate.getSku();
        this.price = productToUpdate.getPrice();
        this.stock = productToUpdate.getStock();
        this.activeStatus = productToUpdate.getStock() != 0 && productToUpdate.isActiveStatus();
        this.updatedOn = new Date(System.currentTimeMillis());
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public boolean isActiveStatus() {
        return activeStatus;
    }

    public void setActiveStatus(boolean activeStatus) {
        this.activeStatus = activeStatus;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    public Date getUpdatedOn() {
        return updatedOn;
    }

    public void setUpdatedOn(Date updatedOn) {
        this.updatedOn = updatedOn;
    }
}
