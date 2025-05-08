package io.hello.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "io.hello.demo.config")
public class AutoConfigurationModuleApplication {
    public static void main(String[] args) {
        SpringApplication.run(AutoConfigurationModuleApplication.class, args);
    }
}
