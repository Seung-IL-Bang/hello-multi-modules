package io.hello.demo.idempotentmodule;

import java.util.concurrent.atomic.AtomicInteger;

public class Account {

    private String accountId;
    private AtomicInteger balance;

    public Account(String accountId, AtomicInteger balance) {
        this.accountId = accountId;
        this.balance = balance;
    }

    public Account(String accountId) {
        this.accountId = accountId;
        this.balance = new AtomicInteger(0);
    }

    public String getAccountId() {
        return accountId;
    }

    public AtomicInteger getBalance() {
        return balance;
    }

    public void withdraw(int amount) {
        if (balance.get() >= amount) {
            balance.addAndGet(-amount);
        } else {
            throw new IllegalArgumentException("Insufficient balance");
        }
    }
}
