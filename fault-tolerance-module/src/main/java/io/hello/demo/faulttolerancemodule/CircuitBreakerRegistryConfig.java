package io.hello.demo.faulttolerancemodule;

import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.time.Duration;
import java.util.concurrent.TimeoutException;

@Configuration
public class CircuitBreakerRegistryConfig {

    @Bean
    public CircuitBreakerRegistry circuitBreakerRegistry() {

        CircuitBreakerConfig circuitBreakerConfig = CircuitBreakerConfig.custom()
                .slidingWindowType(CircuitBreakerConfig.SlidingWindowType.COUNT_BASED)
                .minimumNumberOfCalls(5)
                .failureRateThreshold(50)
                .waitDurationInOpenState(Duration.ofMillis(10000))
                .slidingWindowSize(10)
                .slowCallDurationThreshold(Duration.ofMillis(1000))
                .slowCallRateThreshold(50)
                .permittedNumberOfCallsInHalfOpenState(4)
                .automaticTransitionFromOpenToHalfOpenEnabled(true)
                .recordExceptions(IOException.class, TimeoutException.class, RecordException.class)
                .ignoreExceptions(IgnoreException.class)
                .build();

        return CircuitBreakerRegistry.of(circuitBreakerConfig);
    }

}
