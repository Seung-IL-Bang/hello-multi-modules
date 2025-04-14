package io.hello.demo.faulttolerancemodule.retry;

import io.github.resilience4j.core.IntervalFunction;
import io.github.resilience4j.retry.RetryConfig;
import io.github.resilience4j.retry.RetryRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class RetryRegistryConfig {

    @Bean
    public RetryRegistry retryRegistry() {
        RetryConfig retryConfig = RetryConfig.custom()
                .maxAttempts(5)
//                .waitDuration(Duration.ofMillis(1000)) // wait fixed duration between attempts
                .retryExceptions(RetryException.class)
                .intervalFunction(IntervalFunction.ofExponentialRandomBackoff(
                        Duration.ofMillis(500), // initial interval
                        2, // multiplier
                        0.5, // randomization factor
                        Duration.ofSeconds(10) // max wait duration limit
                ))
                .build();

        return RetryRegistry.of(retryConfig);
    }
}
