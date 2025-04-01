package io.hello.demo.paymentapi.domain;

import io.hello.demo.paymentapi.domain.processor.PaymentProcessor;
import io.hello.demo.paymentapi.domain.processor.v3.DefaultPaymentProcessorV3;
import io.hello.demo.paymentapi.domain.generator.TransactionIdGenerator;
import io.hello.demo.paymentapi.domain.generator.UuidTransactionIdGenerator;
import io.hello.demo.paymentapi.domain.validator.*;
import io.hello.demo.paymentapi.support.error.CoreException;
import io.hello.demo.paymentapi.support.error.ErrorType;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


class PaymentProcessorTest {

    private PaymentProcessor paymentProcessor;

    @BeforeEach
    void setUp() {
        TransactionIdGenerator transactionIdGenerator = new UuidTransactionIdGenerator();

        List<PaymentValidator> validators = List.of(
                new AmountValidator(),
                new CardNumberValidator(),
                new CardExpiryValidator(),
                new CardCvcValidator()
        );

//        paymentProcessor = new DefaultPaymentProcessorV1();
//        paymentProcessor = new DefaultPaymentProcessorV2(validators);
        paymentProcessor = new DefaultPaymentProcessorV3(transactionIdGenerator, validators);
    }


    @DisplayName("올바른 결제 요청에 대해 결제 성공을 반환한다.")
    @Test
    void testPaymentProcess_withValidRequest_shouldReturnApprovedResult() {
        // given
        PaymentRequest paymentRequest = new PaymentRequest(
                10000L,
                "1234-5678-9012-3456",
                "12/25",
                "123",
                "VISA"
        );

        // when
        PaymentResult result = paymentProcessor.process(paymentRequest);

        // then
        assertThat(result.status()).isEqualTo(PaymentStatus.APPROVED);
        assertThat(result.transactionId()).isNotNull();
        assertThat(result.approvedAt()).isNotNull();
        assertThat(result.errorMessage()).isNull();
        assertThat(result.errorCode()).isNull();
    }

    @DisplayName("결제 요청의 결제 금액이 0원 미만인 경우 예외가 발생한다.")
    @Test
    void testPaymentProcess_withInvalidAmount_shouldThrowCoreException() {
        // given
        PaymentRequest paymentRequest = new PaymentRequest(
                -10000L,
                "1234-5678-9012-3456",
                "12/25",
                "123",
                "VISA"
        );

        // when & then
        assertThatThrownBy(() -> paymentProcessor.process(paymentRequest))
                .isInstanceOf(CoreException.class)
                .hasMessage(ErrorType.INVALID_PAYMENT_AMOUNT.getMessage())
                .hasFieldOrPropertyWithValue("errorType", ErrorType.INVALID_PAYMENT_AMOUNT);
    }

    @DisplayName("경계값 테스트: 결제 요청의 결제 금액이 0원인 경우 결제 성공을 반환한다.")
    @Test
    void testPaymentProcess_withZeroAmount_shouldReturnApprovedResult() {
        // given
        PaymentRequest paymentRequest = new PaymentRequest(
                0L,
                "1234-5678-9012-3456",
                "12/25",
                "123",
                "VISA"
        );

        // when
        PaymentResult result = paymentProcessor.process(paymentRequest);

        // then
        assertThat(result.status()).isEqualTo(PaymentStatus.APPROVED);
        assertThat(result.transactionId()).isNotNull();
        assertThat(result.approvedAt()).isNotNull();
        assertThat(result.errorMessage()).isNull();
        assertThat(result.errorCode()).isNull();
    }

    @DisplayName("잘못된 카드 번호로 결제 요청 시 예외를 반환한다.")
    @Test
    void testPaymentProcess_withInvalidCardNumber_shouldThrowCoreException() {
        // given
        PaymentRequest paymentRequest = new PaymentRequest(
                10000L,
                "invalid-card-number",
                "12/25",
                "123",
                "VISA"
        );

        // when & then
        assertThatThrownBy(() -> paymentProcessor.process(paymentRequest))
                .isInstanceOf(CoreException.class)
                .hasMessage(ErrorType.INVALID_PAYMENT_CARD_NUMBER.getMessage())
                .hasFieldOrPropertyWithValue("errorType", ErrorType.INVALID_PAYMENT_CARD_NUMBER);
    }

    @DisplayName("잘못된 카드 유효기간으로 결제 요청 시 예외를 반환한다.")
    @Test
    void testPaymentProcess_withInvalidCardExpiryDate_shouldThrowCoreException() {
        // given
        PaymentRequest paymentRequest = new PaymentRequest(
                10000L,
                "1234-5678-9012-3456",
                "invalid-expiry-date",
                "123",
                "VISA"
        );

        // when & then
        assertThatThrownBy(() -> paymentProcessor.process(paymentRequest))
                .isInstanceOf(CoreException.class)
                .hasMessage(ErrorType.INVALID_PAYMENT_CARD_EXPIRY.getMessage())
                .hasFieldOrPropertyWithValue("errorType", ErrorType.INVALID_PAYMENT_CARD_EXPIRY);
    }

    @DisplayName("잘못된 카드 CVC로 결제 요청 시 예외를 반환한다.")
    @Test
    void testPaymentProcess_withInvalidCardCvc_shouldThrowCoreException() {
        // given
        PaymentRequest paymentRequest = new PaymentRequest(
                10000L,
                "1234-5678-9012-3456",
                "12/25",
                "invalid-cvc",
                "VISA"
        );

        // when & then
        assertThatThrownBy(() -> paymentProcessor.process(paymentRequest))
                .isInstanceOf(CoreException.class)
                .hasMessage(ErrorType.INVALID_PAYMENT_CARD_CVC.getMessage())
                .hasFieldOrPropertyWithValue("errorType", ErrorType.INVALID_PAYMENT_CARD_CVC);
    }
}