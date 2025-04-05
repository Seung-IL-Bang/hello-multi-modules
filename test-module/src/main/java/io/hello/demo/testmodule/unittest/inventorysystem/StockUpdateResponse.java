package io.hello.demo.testmodule.unittest.inventorysystem;

public class StockUpdateResponse {

    private String productId;
    private int currentStock;
    private String message;

    public StockUpdateResponse(String productId, int currentStock, String message) {
        this.productId = productId;
        this.currentStock = currentStock;
        this.message = message;
    }

    // Getters and Setters

    public String getProductId() { return productId; }
    public void setProductId(String productId) { this.productId = productId; }

    public int getCurrentStock() { return currentStock; }
    public void setCurrentStock(int currentStock) { this.currentStock = currentStock; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}
