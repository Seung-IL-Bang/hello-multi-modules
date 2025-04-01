package io.hello.demo.inventoryapi;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class InventoryApiConfig {

    @Bean
    public InventoryService inventoryService() {
        return new DefaultInventoryService();
    }

}
