package io.hello.demo.testmodule.unittest.simplesystem.discountinfo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class DiscountServiceTest {

    private DiscountService discountService;

    @BeforeEach
    void setUp() {
        discountService = new DiscountService();
    }

    @Test
    @DisplayName("유효한 할인 코드를 입력하면 할인이 정확하게 적용된다.")
    void applyDiscount_validCode() {
        double originalAmount = 100.0;
        String validCode = "SUMMER20";
        double expectedAmount = 80.0;
        double actualAmount = discountService.applyDiscount(validCode, originalAmount);
        assertEquals(expectedAmount, actualAmount, 0.001);
    }

    @Test
    @DisplayName("유효한 할인 코드를 사용하면 해당 코드는 사용된 것으로 처리된다.")
    void applyDiscount_validCode_marksAsUsed() {
        double originalAmount = 100.0;
        String validCode = "WELCOME10";
        discountService.applyDiscount(validCode, originalAmount);
        assertThrows(IllegalStateException.class, () -> discountService.applyDiscount(validCode, originalAmount));
    }

    @Test
    @DisplayName("존재하지 않는 할인 코드를 입력하면 IllegalArgumentException이 발생한다.")
    void applyDiscount_invalidCode_throwsException() {
        double originalAmount = 50.0;
        String invalidCode = "INVALIDCODE";
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> discountService.applyDiscount(invalidCode, originalAmount));
        assertEquals("Invalid discount code: " + invalidCode, exception.getMessage());
    }

    @Test
    @DisplayName("만료된 할인 코드를 입력하면 IllegalStateException이 발생한다.")
    void applyDiscount_expiredCode_throwsException() {
        double originalAmount = 100.0;
        String expiredCode = "EXPIRED05";
        IllegalStateException exception = assertThrows(IllegalStateException.class,
                () -> discountService.applyDiscount(expiredCode, originalAmount));
        assertEquals("This discount code has expired: " + expiredCode, exception.getMessage());
    }

    @Test
    @DisplayName("이미 사용된 할인 코드를 입력하면 IllegalStateException이 발생한다.")
    void applyDiscount_usedCode_throwsException() {
        double originalAmount = 75.0;
        String usedCode = "USEDCODE";
        IllegalStateException exception = assertThrows(IllegalStateException.class,
                () -> discountService.applyDiscount(usedCode, originalAmount));
        assertEquals("This discount code has already been used: " + usedCode, exception.getMessage());
    }

    @ParameterizedTest
    @CsvSource({"-0.1, 100.0", "1.1, 50.0"})
    @DisplayName("할인율이 유효 범위를 벗어나면 IllegalArgumentException이 발생한다.")
    void applyDiscount_invalidRate_throwsException(double invalidRate, double orderAmount) {
        // 테스트를 위한 임시 DiscountService (기존 맵에 유효하지 않은 할인율 추가)
        DiscountService tempService = new DiscountService();
        tempService.addDiscountCode("INVALID_RATE", new DiscountService.DiscountInfo(invalidRate, LocalDate.of(2025, 12, 31), false));
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> tempService.applyDiscount("INVALID_RATE", orderAmount));
        assertTrue(exception.getMessage().contains("Invalid discount rate:"));
    }

    @ParameterizedTest
    @CsvSource({"SUMMER20, summer20", "WELCOME10, Welcome10"})
    @DisplayName("할인 코드는 대소문자를 구분한다 (구현에 따라 테스트 조정 필요).")
    void applyDiscount_caseSensitivity(String validCode, String differentCaseCode) {
        double originalAmount = 100.0;
        assertDoesNotThrow(() -> discountService.applyDiscount(validCode, originalAmount));
        assertThrows(IllegalArgumentException.class, () -> discountService.applyDiscount(differentCaseCode, originalAmount));
    }

    @Test
    @DisplayName("유효한 할인 코드를 여러 번 다른 주문에 적용할 수 있다.")
    void applyDiscount_validCodeMultipleOrders() {
        double amount1 = 100.0;
        double amount2 = 50.0;
        String validCode = "WELCOME10";
        assertDoesNotThrow(() -> discountService.applyDiscount(validCode, amount1));
        assertThrows(IllegalStateException.class, () -> discountService.applyDiscount(validCode, amount2)); // 이미 사용되었으므로 실패
    }

}