package com.example.inventoryservice.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "inventory")
public class Inventory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long orderId;

    @Column(nullable = false, length = 255)
    private String itemName;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false, updatable = false)
    private LocalDateTime reservedAt;

    @Column(nullable = false, length = 50)
    private String status;

    public Inventory() {
        this.reservedAt = LocalDateTime.now();
        this.status = "RESERVED";
    }

    public Inventory(Long orderId, String itemName, Integer quantity) {
        this();
        this.orderId = orderId;
        this.itemName = itemName;
        this.quantity = quantity;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public LocalDateTime getReservedAt() {
        return reservedAt;
    }

    public void setReservedAt(LocalDateTime reservedAt) {
        this.reservedAt = reservedAt;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Inventory{" +
                "id=" + id +
                ", orderId=" + orderId +
                ", itemName='" + itemName + '\'' +
                ", quantity=" + quantity +
                ", reservedAt=" + reservedAt +
                ", status='" + status + '\'' +
                '}';
    }
}
