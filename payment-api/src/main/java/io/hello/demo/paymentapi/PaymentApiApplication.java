package io.hello.demo.paymentapi;

import io.hello.demo.inventoryapi.InventoryApiConfig;
import io.hello.demo.sseapi.SseService;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import(value = {InventoryApiConfig.class, SseService.class})
public class PaymentApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(PaymentApiApplication.class, args);
    }

}

