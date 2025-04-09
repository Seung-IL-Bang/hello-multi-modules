package io.hello.demo.faulttolerancemodule.retry;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/resilience4j/retry")
public class RetryController {

    private final RetryService retryService;

    public RetryController(RetryService retryService) {
        this.retryService = retryService;
    }

    @GetMapping("/api/v1/call/without-resilience4j")
    public String apiCallWithoutResilience4j(@RequestParam("p") String p) {
        return retryService.processWithoutResilience4j(p);
    }

    @GetMapping("/api/v1/call/with-resilience4j")
    public String apiCallWithResilience4j(@RequestParam("p") String p) {
        return retryService.processWithResilience4j(p);
    }

}
