package io.hello.demo.idempotentmodule;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class AccountStore {

    private final Logger log = LoggerFactory.getLogger(AccountStore.class);
    private final Map<String, Account> accountStore = new ConcurrentHashMap<>();

    public void save(Account account) {
        if (account == null) {
            log.info("Account cannot be null");
            throw new IllegalArgumentException("Account cannot be null");
        }

        if (account.getAccountId() == null || account.getAccountId().isBlank()) {
            log.info("Account ID cannot be null or blank");
            throw new IllegalArgumentException("Account ID cannot be null or blank");
        }

        if (account.getBalance() == null) {
            log.info("Account balance cannot be null");
            account = new Account(account.getAccountId());
            log.info("Setting default balance to 0 for account {}", account.getAccountId());
        }

        if (!exists(account.getAccountId())) {
            accountStore.put(account.getAccountId(), account);
        } else {
            log.info("Account {} already exists. Updating the existing account.", account.getAccountId());
            update(account);
        }
    }

    public void update(Account account) {
        if (account == null) {
            log.info("Account cannot be null");
            throw new IllegalArgumentException("Account cannot be null");
        }

        if (account.getAccountId() == null || account.getAccountId().isBlank()) {
            log.info("Account ID cannot be null or blank");
            throw new IllegalArgumentException("Account ID cannot be null or blank");
        }

        if (account.getBalance() == null) {
            log.info("Account balance cannot be null");
            throw new IllegalArgumentException("Account balance cannot be null");
        }

        if (exists(account.getAccountId())) {
            accountStore.put(account.getAccountId(), account);
        } else {
            log.info("Account {} does not exist. Creating a new account.", account.getAccountId());
            throw new IllegalArgumentException("Account does not exist");
        }
    }

    public Account get(String accountId) {
        return accountStore.getOrDefault(accountId, null);
    }

    public boolean exists(String accountId) {
        return accountStore.containsKey(accountId);
    }
}
