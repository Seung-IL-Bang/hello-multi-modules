package io.hello.demo.idempotentmodule;

public class AccountQueryHandler {

    private final AccountStore accountStore;

    public AccountQueryHandler(AccountStore accountStore) {
        this.accountStore = accountStore;
    }

    public Account getAccount(String accountId) {
        Account account = accountStore.get(accountId);

        if (account == null) {
            throw new IllegalArgumentException("Account not found");
        }

        return account;
    }

    public int getBalance(String accountId) {
        return accountStore.get(accountId).getBalance().intValue();
    }
}
