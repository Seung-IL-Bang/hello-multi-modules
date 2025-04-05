package io.hello.demo.testmodule.unittest.inventorysystem;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.stream.Collectors;

@Service
public class InventoryService {

    private final ProductRepository productRepository;
    private final LockManager lockManager;

    public InventoryService(ProductRepository productRepository, LockManager lockManager) {
        this.productRepository = productRepository;
        this.lockManager = lockManager;
    }

    @Transactional(readOnly = true)
    public Product getProduct(String productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product not found: " + productId));
    }

    @Transactional(readOnly = true)
    public List<Product> getProductsWithLowStock(int threshold) {
        return productRepository.findAll().stream()
                .filter(product -> product.getStock() <= threshold)
                .collect(Collectors.toList());
    }

    @Transactional
    public StockUpdateResponse updateStock(StockUpdateRequest request) {
        String productId = request.getProductId();
        int quantity = request.getQuantity();

        // 해당 상품에 대한 락 획득
        Lock lock = lockManager.getLock(productId);
        lock.lock();

        try {
            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new ProductNotFoundException("Product not found: " + productId));

            if (quantity < 0 && Math.abs(quantity) > product.getStock()) {
                throw new OutOfStockException("Not enough stock for product: " + productId);
            }

            // 재고 업데이트
            product.updateStock(quantity);
            productRepository.save(product);

            return new StockUpdateResponse(productId, product.getStock(), "Stock updated successfully");
        } finally {
            lock.unlock();
        }
    }

    @Transactional
    public void restockProducts(List<StockUpdateRequest> requests) {
        for (StockUpdateRequest request : requests) {
            if (request.getQuantity() <= 0) {
                throw new IllegalArgumentException("Restock quantity must be positive");
            }

            updateStock(request);
        }
    }

    @Transactional
    public boolean reserveStock(String productId, int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Reservation quantity must be positive");
        }

        Lock lock = lockManager.getLock(productId);
        lock.lock();

        try {
            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new ProductNotFoundException("Product not found: " + productId));

            if (product.getStock() >= quantity) {
                product.updateStock(-quantity);
                productRepository.save(product);
                return true;
            } else {
                return false;
            }
        } finally {
            lock.unlock();
        }
    }


}
