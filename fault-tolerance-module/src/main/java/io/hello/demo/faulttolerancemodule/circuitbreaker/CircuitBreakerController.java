package io.hello.demo.faulttolerancemodule.circuitbreaker;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/resilience4j")
public class CircuitBreakerController {

    private final CircuitBreakerService circuitBreakerService;
    private final CircuitBreakerAnnotationService circuitBreakerAnnotationService;

    public CircuitBreakerController(CircuitBreakerService circuitBreakerService,
                                     CircuitBreakerAnnotationService circuitBreakerAnnotationService) {
        this.circuitBreakerService = circuitBreakerService;
        this.circuitBreakerAnnotationService = circuitBreakerAnnotationService;
    }

    @GetMapping("/api/v1/call")
    public String apiCall(@RequestParam("p") String p) {
        return circuitBreakerAnnotationService.process(p);
    }
}
