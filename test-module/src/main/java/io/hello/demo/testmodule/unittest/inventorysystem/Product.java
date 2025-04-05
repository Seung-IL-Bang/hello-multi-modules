package io.hello.demo.testmodule.unittest.inventorysystem;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Product {
    private String id;
    private String name;
    private BigDecimal price;
    private int stock;
    private String merchantId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Getters and Setters

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }

    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }

    public String getMerchantId() { return merchantId; }
    public void setMerchantId(String merchantId) { this.merchantId = merchantId; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    // 비즈니스 메소드

    public void updateStock(int quantity) {
        this.stock += quantity;
        this.updatedAt = LocalDateTime.now();
    }

    public boolean isAvailable() {
        return this.stock > 0;
    }

    public boolean hasLowStock(int threshold) {
        return this.stock <= threshold;
    }
}
