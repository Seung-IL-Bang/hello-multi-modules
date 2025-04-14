package io.hello.demo.idempotentmodule;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicInteger;

public class AccountAggregate {

    private final Logger log = LoggerFactory.getLogger(AccountAggregate.class);

    private final AtomicInteger balance = new AtomicInteger(1000);

    public void apply(MoneyWithdrawnEvent event) {
        if (event.amount() > balance.intValue()) {
            log.warn("Account {}: Insufficient funds for withdrawal of {}. Current balance: {}", event.accountId(), event.amount(), balance);
            return;
        }
        balance.getAndSet(balance.intValue() - event.amount());
        log.info("Account {}: Withdrawn {}. New balance: {}", event.accountId(), event.amount(), balance.get());
    }
}
