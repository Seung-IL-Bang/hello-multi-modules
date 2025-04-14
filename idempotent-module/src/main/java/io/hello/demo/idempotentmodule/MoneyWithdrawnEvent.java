package io.hello.demo.idempotentmodule;

public record MoneyWithdrawnEvent(
    String eventId,
    String accountId,
    int amount
) {
}
