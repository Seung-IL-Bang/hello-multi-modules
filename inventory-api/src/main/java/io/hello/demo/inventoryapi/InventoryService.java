package io.hello.demo.inventoryapi;

public interface InventoryService {
    boolean checkAndReserveInventory(String productId, int quantity);
    int getInventory(String productId);
}
