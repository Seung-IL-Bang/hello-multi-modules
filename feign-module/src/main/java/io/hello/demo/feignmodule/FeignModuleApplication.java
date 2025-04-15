package io.hello.demo.feignmodule;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients(basePackages = "io.hello.demo.feignmodule")
public class FeignModuleApplication {

    public static void main(String[] args) {
        SpringApplication.run(FeignModuleApplication.class, args);
    }

}
