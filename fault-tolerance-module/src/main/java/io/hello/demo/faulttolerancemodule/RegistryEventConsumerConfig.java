package io.hello.demo.faulttolerancemodule;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.core.registry.EntryAddedEvent;
import io.github.resilience4j.core.registry.EntryRemovedEvent;
import io.github.resilience4j.core.registry.EntryReplacedEvent;
import io.github.resilience4j.core.registry.RegistryEventConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RegistryEventConsumerConfig {

    private final Logger log = LoggerFactory.getLogger(getClass());


    @Bean
    public RegistryEventConsumer<CircuitBreaker> registryEventConsumer() {
        return new RegistryEventConsumer<>() {
            @Override
            public void onEntryAddedEvent(EntryAddedEvent<CircuitBreaker> entryAddedEvent) {
                CircuitBreaker circuitBreaker = entryAddedEvent.getAddedEntry();
                log.info("Circuit Breaker '{}' added to registry", circuitBreaker.getName());

                // Subscribe to all circuit breaker events
                circuitBreaker.getEventPublisher().onEvent(event ->
                        log.info("Circuit Breaker '{}' event: {}", circuitBreaker.getName(), event.toString()));

                // Subscribe specifically to state transition events
                circuitBreaker.getEventPublisher().onStateTransition(event ->
                        log.info("Circuit Breaker '{}' state changed from {} to {}",
                                circuitBreaker.getName(),
                                event.getStateTransition().getFromState(),
                                event.getStateTransition().getToState()));

                // Subscribe to specific events
                circuitBreaker.getEventPublisher().onSuccess(event ->
                        log.debug("Circuit Breaker '{}' recorded successful call", circuitBreaker.getName()));

                circuitBreaker.getEventPublisher().onError(event ->
                        log.info("Circuit Breaker '{}' recorded failed call: {}",
                                circuitBreaker.getName(), event.getThrowable().toString()));

                circuitBreaker.getEventPublisher().onSlowCallRateExceeded(event ->
                        log.info("Circuit Breaker '{}' slow call rate exceeded: {}",
                                circuitBreaker.getName(), event.getSlowCallRate()));

                circuitBreaker.getEventPublisher().onFailureRateExceeded(event ->
                        log.info("Circuit Breaker '{}' failure rate exceeded: {}",
                                circuitBreaker.getName(), event.getFailureRate()));
            }

            @Override
            public void onEntryRemovedEvent(EntryRemovedEvent<CircuitBreaker> entryRemoveEvent) {
                log.info("Circuit Breaker '{}' removed from registry",
                        entryRemoveEvent.getRemovedEntry().getName());
            }

            @Override
            public void onEntryReplacedEvent(EntryReplacedEvent<CircuitBreaker> entryReplacedEvent) {
                log.info("Circuit Breaker '{}' replaced in registry",
                        entryReplacedEvent.getNewEntry().getName());
            }
        };
    }

}
