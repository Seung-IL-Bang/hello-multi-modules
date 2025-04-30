package io.hello.demo;

import io.hello.demo.config.OrderConfigV0;
import io.hello.demo.config.OrderConfigV1;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;

//@Import(OrderConfigV0.class)
@Import(OrderConfigV1.class)
@SpringBootApplication(scanBasePackages = "io.hello.demo.controller")
public class MonitoringModuleApplication {
    public static void main(String[] args) {
        SpringApplication.run(MonitoringModuleApplication.class, args);
    }
}
