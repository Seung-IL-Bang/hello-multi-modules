package io.hello.demo.idempotentmodule;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

class AccountQueryHandlerTest {

    public static final String ACC_ID = "acc-456";
    public static final int INITIAL_ACC_AMOUNT = 1000;
    private AccountQueryHandler accountQueryHandler;

    @BeforeEach
    void setUp() {
        EventStore eventStore = new EventStore();
        AccountStore accountStore = new AccountStore();
        Account account = new Account(ACC_ID, new AtomicInteger(INITIAL_ACC_AMOUNT));
        accountStore.save(account);

        accountQueryHandler = new AccountQueryHandler(accountStore);
    }

    @DisplayName("계좌 조회 테스트 - 정상 조회")
    @Test
    void testGetAccount() {
        // given & when
        Account account = accountQueryHandler.getAccount(ACC_ID);

        // then
        assertNotNull(account);
        assertEquals(ACC_ID, account.getAccountId());
        assertEquals(INITIAL_ACC_AMOUNT, account.getBalance().intValue());
    }

    @DisplayName("계좌 조회 테스트 - 계좌 없음")
    @Test
    void testGetAccountNotFound() {
        // given
        String nonExistentAccountId = "non-existent-acc";

        // when & then
        assertThatThrownBy(() -> accountQueryHandler.getAccount(nonExistentAccountId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Account not found");
    }

    @DisplayName("계좌 잔액 조회 테스트 - 정상 조회")
    @Test
    void testGetBalance() {
        // given & when
        int balance = accountQueryHandler.getBalance(ACC_ID);

        // then
        assertEquals(INITIAL_ACC_AMOUNT, balance);
    }
}