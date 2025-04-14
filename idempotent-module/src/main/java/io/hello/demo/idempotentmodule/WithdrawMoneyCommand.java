package io.hello.demo.idempotentmodule;

public record WithdrawMoneyCommand(
    String commandId,
    String accountId,
    int amount
) {
}
