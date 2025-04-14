package io.hello.demo.faulttolerancemodule.ratelimiter;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SimpleController {

    private final SimpleService simpleService;

    public SimpleController(SimpleService simpleService) {
        this.simpleService = simpleService;
    }

    @GetMapping("/api/v1/rate-limit")
    public String rateLimiter(@RequestParam("key") String key) {
        return simpleService.process(key);
    }
}
