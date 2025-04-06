package io.hello.demo.testmodule.unittest.simplesystem.orderservice;

import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository {

    Order save(Order order);

    void deleteById(Long orderId);
}
