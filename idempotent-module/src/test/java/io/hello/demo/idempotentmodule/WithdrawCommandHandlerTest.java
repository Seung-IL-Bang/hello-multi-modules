package io.hello.demo.idempotentmodule;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class WithdrawCommandHandlerTest {

    public static final String ACC_ID = "acc-456";
    public static final int INITIAL_ACC_AMOUNT = 1000;
    private WithdrawCommandHandler withdrawCommandHandler;
    private AccountQueryHandler accountQueryHandler;

    @BeforeEach
    void setUp() {
        EventStore eventStore = new EventStore();
        AccountStore accountStore = new AccountStore();
        Account account = new Account(ACC_ID, new AtomicInteger(INITIAL_ACC_AMOUNT));
        accountStore.save(account);

        AccountAggregate accountAggregate = new AccountAggregate(accountStore);

        accountQueryHandler = new AccountQueryHandler(accountStore);
        withdrawCommandHandler = new WithdrawCommandHandler(eventStore, accountAggregate);
    }

    @DisplayName("출금 처리 테스트 - 이벤트 정상 완료")
    @Test
    void testHandle() {
        // given
        WithdrawMoneyCommand command = new WithdrawMoneyCommand("cmd-123", ACC_ID, 100);

        // when
        withdrawCommandHandler.handle(command);

        // then
        Account account = accountQueryHandler.getAccount(ACC_ID);
        assertThat(account.getBalance().intValue()).isEqualTo(INITIAL_ACC_AMOUNT - 100);
    }

    @DisplayName("출금 처리 테스트 - 잔액 부족")
    @Test
    void testHandleInsufficientBalance() {
        // given
        WithdrawMoneyCommand command = new WithdrawMoneyCommand("cmd-123", ACC_ID, 2000);

        // when & then
        assertThatThrownBy(() -> withdrawCommandHandler.handle(command))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Insufficient balance");

        Account account = accountQueryHandler.getAccount(ACC_ID);
        assertThat(account.getBalance().intValue()).isEqualTo(INITIAL_ACC_AMOUNT);
    }

    @DisplayName("출금 중복 처리 테스트 - 이벤트 중복 처리 방지")
    @Test
    void testHandleDuplicateEvent() {
        // given
        WithdrawMoneyCommand command = new WithdrawMoneyCommand("cmd-123", ACC_ID, 100);

        // when
        withdrawCommandHandler.handle(command);

        // then
        assertThatThrownBy(() -> withdrawCommandHandler.handle(command))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Event already processed");

        Account account = accountQueryHandler.getAccount(ACC_ID);
        assertThat(account.getBalance().intValue()).isEqualTo(INITIAL_ACC_AMOUNT - 100);
    }
}