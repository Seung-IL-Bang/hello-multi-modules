package io.hello.demo.faulttolerancemodule.circuitbreaker;

import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class CircuitBreakerAnnotationService {

    private static final String SIMPLE_CIRCUIT_BREAKER_CONFIG = "simpleCircuitBreakerConfig";
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private final CircuitBreakerRegistry circuitBreakerRegistry;

    public CircuitBreakerAnnotationService(CircuitBreakerRegistry circuitBreakerRegistry) {
        this.circuitBreakerRegistry = circuitBreakerRegistry;
    }

    @PostConstruct
    public void registerEventListener() {
        circuitBreakerRegistry.circuitBreaker(SIMPLE_CIRCUIT_BREAKER_CONFIG)
                .getEventPublisher()
                .onStateTransition(event -> log.info("CircuitBreaker State [{}] -> [{}]",
                        event.getStateTransition().getFromState(),
                        event.getStateTransition().getToState()));
    }

    @CircuitBreaker(name = SIMPLE_CIRCUIT_BREAKER_CONFIG, fallbackMethod = "fallback")
    public String process(String param) {
        return processInternal(param);
    }

    private String processInternal(String param) {
        try {
            if ("a".equals(param))
                throw new RecordException("record exception");
            else if ("b".equals(param))
                throw new IgnoreException("ignore exception");
            else if ("c".equals(param)) // 3초 이상 걸리는 경우도 실패로 간주
                Thread.sleep(1100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Thread was interrupted", e);
        }
        log.info("process success! your request is " + param);
        return "Param: " + param;
    }

    private String fallback(String param, Exception ex) {
        // fallback은 ignoreException이 발생해도 실행된다.
        log.info("fallback! your request is " + param);
        return "Recovered: " + ex.toString();
    }
}
