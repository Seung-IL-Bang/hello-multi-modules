package io.hello.demo.idempotentmodule;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WithdrawCommandHandler {

    private final Logger log = LoggerFactory.getLogger(WithdrawCommandHandler.class);

    private final EventStore eventStore;
    private final AccountAggregate accountAggregate;

    public WithdrawCommandHandler(EventStore eventStore, AccountAggregate accountAggregate) {
        this.eventStore = eventStore;
        this.accountAggregate = accountAggregate;
    }

    public void handle(WithdrawMoneyCommand command) {
        if (eventStore.exists(command.commandId())) {
            log.warn("Command already processed: {}", command.commandId());
            return;
        }

        MoneyWithdrawnEvent event = new MoneyWithdrawnEvent(
                command.commandId(), command.accountId(), command.amount()
        );

        eventStore.append(event);
        accountAggregate.apply(event);
    }
}
