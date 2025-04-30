package io.hello.demo.config;

import io.hello.demo.service.OrderService;
import io.hello.demo.service.OrderServiceV0;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OrderConfigV0 {

    @Bean
    public OrderService orderService() {
        return new OrderServiceV0();
    }
}
