package io.hello.demo.idempotentmodule;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AccountAggregate {

    private final Logger log = LoggerFactory.getLogger(AccountAggregate.class);

    private final AccountStore accountStore;

    public AccountAggregate(AccountStore accountStore) {
        this.accountStore = accountStore;
    }

    public synchronized void apply(MoneyWithdrawnEvent event) {
        Account account = accountStore.get(event.accountId());
        account.withdraw(event.amount());
        accountStore.update(account);
        log.info("Account {}: Withdrawn {}. New balance: {}", event.accountId(), event.amount(), account.getBalance());
    }
}
