package io.hello.demo.inventoryapi;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class DefaultInventoryService implements InventoryService {

    private final Map<String, Integer> productInventory = new ConcurrentHashMap<>();
    private final Map<String, Lock> productLocks = new ConcurrentHashMap<>();

    public DefaultInventoryService() {
        productInventory.put("product-1", 100);
        productInventory.put("product-2", 50);
    }

    @Override
    public boolean checkAndReserveInventory(String productId, int quantity) {

        Lock lock = productLocks.computeIfAbsent(productId, k -> new ReentrantLock());

        try {
            lock.lock();

            Integer currentStock = productInventory.getOrDefault(productId, 0);
            Thread.sleep(10); // 동시성 문제를 재현하기 위해 잠시 대기
            if (currentStock < quantity) {
                return false; // 재고 부족
            }

            productInventory.put(productId, currentStock - quantity); // 재고 차감
            return true;
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            lock.unlock();
        }
    }

    public int getInventory(String productId) {
        return productInventory.getOrDefault(productId, 0);
    }
}
