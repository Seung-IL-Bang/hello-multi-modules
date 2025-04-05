package io.hello.demo.testmodule.unittest.inventorysystem;

public class StockUpdateRequest {

    private String productId;
    private int quantity;

    // Getters and Setters

    public String getProductId() { return productId; }
    public void setProductId(String productId) { this.productId = productId; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
}
