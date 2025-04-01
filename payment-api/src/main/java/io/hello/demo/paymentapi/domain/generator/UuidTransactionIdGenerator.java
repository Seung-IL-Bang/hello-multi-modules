package io.hello.demo.paymentapi.domain.generator;

import org.springframework.stereotype.Component;

import static java.util.UUID.randomUUID;

@Component
public class UuidTransactionIdGenerator implements TransactionIdGenerator {
    public String generate() {
        return randomUUID().toString();
    }
}
