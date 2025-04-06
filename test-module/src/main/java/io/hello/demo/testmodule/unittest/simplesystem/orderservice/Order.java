package io.hello.demo.testmodule.unittest.simplesystem.orderservice;

public class Order {

    private String productId;
    private int quantity;

    public Order(String productId, int quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public String getProductId() {
        return productId;
    }

    public int getQuantity() {
        return quantity;
    }
}
