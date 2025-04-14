package io.hello.demo.idempotentmodule;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

class AccountStoreTest {

    public static final String ACC_ID = "acc-456";
    public static final int INITIAL_ACC_AMOUNT = 1000;
    private AccountStore accountStore;

    @BeforeEach
    void setUp() {
        accountStore = new AccountStore();
    }

    @DisplayName("계좌 조회 테스트 - 정상 조회")
    @Test
    void testGetAccount() {
        // given
        Account account = new Account(ACC_ID, new AtomicInteger(INITIAL_ACC_AMOUNT));
        accountStore.save(account);

        // when
        Account foundAccount = accountStore.get(ACC_ID);

        // then
        assertNotNull(foundAccount);
        assertEquals(ACC_ID, foundAccount.getAccountId());
        assertEquals(INITIAL_ACC_AMOUNT, foundAccount.getBalance().intValue());
    }

    @DisplayName("계좌 조회 테스트 - 계좌 없음")
    @Test
    void testGetAccountNotFound() {
        // given
        String nonExistentAccountId = "non-existent-acc";

        // when & then
        Account account = accountStore.get(nonExistentAccountId);
        assertNull(account);
    }

    @DisplayName("계좌 저장 테스트 - 정상 저장")
    @Test
    void testSave() {
        // given
        Account account = new Account(ACC_ID, new AtomicInteger(INITIAL_ACC_AMOUNT));

        // when
        accountStore.save(account);

        // then
        Account foundAccount = accountStore.get(ACC_ID);
        assertNotNull(foundAccount);
        assertEquals(ACC_ID, foundAccount.getAccountId());
        assertEquals(INITIAL_ACC_AMOUNT, foundAccount.getBalance().intValue());
    }

    @DisplayName("계좌 저장 테스트 - 계좌 잔액 null 설정 - 기본값 설정")
    @Test
    void testSaveWithNullBalance() {
        // given
        Account account = new Account(ACC_ID, null);

        // when
        accountStore.save(account);

        // then
        Account foundAccount = accountStore.get(ACC_ID);
        assertNotNull(foundAccount);
        assertEquals(ACC_ID, foundAccount.getAccountId());
        assertEquals(0, foundAccount.getBalance().intValue());
    }


    @DisplayName("계좌 저장 테스트 - 계좌 잔액 기본값 설정")
    @Test
    void testSaveWithDefaultBalance() {
        // given
        Account account = new Account(ACC_ID);

        // when
        accountStore.save(account);

        // then
        Account foundAccount = accountStore.get(ACC_ID);
        assertNotNull(foundAccount);
        assertEquals(ACC_ID, foundAccount.getAccountId());
        assertEquals(0, foundAccount.getBalance().intValue());
    }

    @DisplayName("계좌 저장 테스트 - Account NULL 예외 발생")
    @Test
    void testSaveNullAccount() {
        // given
        Account account = null;

        // when & then
        assertThatThrownBy(() -> accountStore.save(account))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Account cannot be null");
    }

    @DisplayName("계좌 저장 테스트 - Account ID NullAndEmpty 예외 발생")
    @ParameterizedTest
    @NullAndEmptySource
    void testSaveNullAccountId(String accountId) {
        // given
        Account account = new Account(accountId, new AtomicInteger(INITIAL_ACC_AMOUNT));

        // when & then
        assertThatThrownBy(() -> accountStore.save(account))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Account ID cannot be null or blank");
    }

    @DisplayName("계좌 저장 테스트 - Account 존재 여부 확인 - 존재하는 계좌")
    @Test
    void testSaveAccountExists() {
        // given
        Account account = new Account(ACC_ID, new AtomicInteger(INITIAL_ACC_AMOUNT));
        accountStore.save(account);

        // when
        boolean exists = accountStore.exists(ACC_ID);

        // then
        assertTrue(exists);
    }

    @DisplayName("계좌 저장 테스트 - Account 존재 여부 확인 - 존재하지 않는 계좌")
    @Test
    void testSaveAccountNotExists() {
        // given
        Account account = new Account(ACC_ID, new AtomicInteger(INITIAL_ACC_AMOUNT));
        accountStore.save(account);
        String nonExistentAccountId = "non-existent-acc";

        // when
        boolean exists = accountStore.exists(nonExistentAccountId);

        // then
        assertFalse(exists);
    }

    @DisplayName("계좌 저장 테스트 - 중복 계좌 저장 시 업데이트 로직 수행")
    @Test
    void testSaveDuplicateAccount() {
        // given
        Account account = new Account(ACC_ID, new AtomicInteger(INITIAL_ACC_AMOUNT));
        accountStore.save(account);

        // when
        Account updatedAccount = new Account(ACC_ID, new AtomicInteger(INITIAL_ACC_AMOUNT + 500));
        accountStore.save(updatedAccount);

        // then
        Account foundAccount = accountStore.get(ACC_ID);
        assertNotNull(foundAccount);
        assertEquals(ACC_ID, foundAccount.getAccountId());
        assertEquals(INITIAL_ACC_AMOUNT + 500, foundAccount.getBalance().intValue());
    }

    @DisplayName("계좌 업데이트 테스트 - 정상 업데이트")
    @Test
    void testUpdate() {
        // given
        Account account = new Account(ACC_ID, new AtomicInteger(INITIAL_ACC_AMOUNT));
        accountStore.save(account);

        // when
        Account updatedAccount = new Account(ACC_ID, new AtomicInteger(INITIAL_ACC_AMOUNT + 500));
        accountStore.update(updatedAccount);

        // then
        Account foundAccount = accountStore.get(ACC_ID);
        assertNotNull(foundAccount);
        assertEquals(ACC_ID, foundAccount.getAccountId());
        assertEquals(INITIAL_ACC_AMOUNT + 500, foundAccount.getBalance().intValue());
    }

    @DisplayName("계좌 업데이트 테스트 - Account NULL 예외 발생")
    @Test
    void testUpdateNullAccount() {
        // given
        Account account = null;

        // when & then
        assertThatThrownBy(() -> accountStore.update(account))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Account cannot be null");
    }

    @DisplayName("계좌 업데이트 테스트 - Account ID NullAndEmpty 예외 발생")
    @ParameterizedTest
    @NullAndEmptySource
    void testUpdateNullAccountId(String accountId) {
        // given
        Account account = new Account(accountId, new AtomicInteger(INITIAL_ACC_AMOUNT));

        // when & then
        assertThatThrownBy(() -> accountStore.update(account))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Account ID cannot be null or blank");
    }

    @DisplayName("계좌 업데이트 테스트 - Account 잔액 NULL 예외 발생")
    @Test
    void testUpdateNullBalance() {
        // given
        Account account = new Account(ACC_ID, new AtomicInteger(INITIAL_ACC_AMOUNT));
        accountStore.save(account);
        Account updatedAccount = new Account(ACC_ID, null);

        // when & then
        assertThatThrownBy(() -> accountStore.update(updatedAccount))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Account balance cannot be null");
    }

    @DisplayName("계좌 업데이트 테스트 - 계좌 없음 예외 발생")
    @Test
    void testUpdateAccountNotFound() {
        // given
        Account account = new Account(ACC_ID, new AtomicInteger(INITIAL_ACC_AMOUNT));
        accountStore.save(account);
        String nonExistentAccountId = "non-existent-acc";
        Account updatedAccount = new Account(nonExistentAccountId, new AtomicInteger(INITIAL_ACC_AMOUNT + 500));

        // when & then
        assertThatThrownBy(() -> accountStore.update(updatedAccount))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Account does not exist");
    }

}