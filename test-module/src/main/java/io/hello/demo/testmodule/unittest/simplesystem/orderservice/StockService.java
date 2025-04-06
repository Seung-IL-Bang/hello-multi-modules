package io.hello.demo.testmodule.unittest.simplesystem.orderservice;

import org.springframework.stereotype.Service;

@Service
public interface StockService {

    boolean hasStock(String productId, int quantity);
}
