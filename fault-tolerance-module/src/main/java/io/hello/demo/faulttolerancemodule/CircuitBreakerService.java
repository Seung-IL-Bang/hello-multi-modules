package io.hello.demo.faulttolerancemodule;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import static org.slf4j.LoggerFactory.*;

@Service
public class CircuitBreakerService {

    private final Logger log = getLogger(this.getClass());
    private final CircuitBreaker circuitBreaker;

    public CircuitBreakerService(CircuitBreakerRegistry circuitBreakerRegistry) {
        this.circuitBreaker = circuitBreakerRegistry.circuitBreaker("hello-circuit");
        this.circuitBreaker.getEventPublisher().onStateTransition(
                event -> log.info("CircuitBreaker State [{}] -> [{}]",
                            event.getStateTransition().getFromState(),
                            event.getStateTransition().getToState())
        );
    }

    private static String processInternal(String param) {
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
        return "Param: " + param;
    }

    public String process(String param) {
        return circuitBreaker.executeSupplier(() -> processInternal(param));
    }

}
