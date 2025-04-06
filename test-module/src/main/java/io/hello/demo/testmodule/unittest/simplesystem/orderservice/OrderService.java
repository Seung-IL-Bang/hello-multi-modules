package io.hello.demo.testmodule.unittest.simplesystem.orderservice;

import org.springframework.stereotype.Service;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final StockService stockService;

    public OrderService(OrderRepository orderRepository, StockService stockService) {
        this.orderRepository = orderRepository;
        this.stockService = stockService;
    }

    public Order createOrder(String productId, int quantity) {
        if (!stockService.hasStock(productId, quantity)) {
            throw new IllegalStateException("재고 부족");
        }
        Order order = new Order(productId, quantity);
        return orderRepository.save(order);
    }

    public void cancelOrder(Long orderId) {
        orderRepository.deleteById(orderId);
    }
}
