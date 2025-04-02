package io.hello.demo.paymentapi.domain;

import io.hello.demo.paymentapi.domain.generator.TransactionIdGenerator;
import io.hello.demo.paymentapi.domain.generator.UuidTransactionIdGenerator;
import io.hello.demo.paymentapi.domain.method.*;
import io.hello.demo.paymentapi.domain.processor.PaymentProcessorV2;
import io.hello.demo.paymentapi.domain.processor.v4.DefaultPaymentProcessorV4;
import io.hello.demo.paymentapi.domain.request.v2.CreditCardPaymentRequest;
import io.hello.demo.paymentapi.domain.validator.v2.PaymentMethodValidator;
import io.hello.demo.paymentapi.domain.validator.v2.creditcard.AmountValidator;
import io.hello.demo.paymentapi.domain.validator.v2.creditcard.CardCvcValidator;
import io.hello.demo.paymentapi.domain.validator.v2.creditcard.CardExpiryValidator;
import io.hello.demo.paymentapi.domain.validator.v2.creditcard.CardNumberValidator;
import io.hello.demo.paymentapi.support.error.ErrorType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


class CreditCardPaymentMethodTest {

    private PaymentProcessorV2 paymentProcessor;

    @BeforeEach
    void setUp() {
        TransactionIdGenerator transactionIdGenerator = new UuidTransactionIdGenerator();
        List<PaymentMethodValidator> validators = List.of(
                new AmountValidator(),
                new CardNumberValidator(),
                new CardExpiryValidator(),
                new CardCvcValidator()
        );
        List<PaymentMethod> paymentMethods = List.of(
                new CreditCardPaymentMethod(validators, transactionIdGenerator),
                new VirtualAccountPaymentMethod(),
                new MobilePaymentMethod()
        );

        PaymentMethodFactory paymentMethodFactory = new PaymentMethodFactory(paymentMethods);
        paymentProcessor = new DefaultPaymentProcessorV4(paymentMethodFactory);
    }


    @DisplayName("올바른 결제 요청에 대해 결제 성공을 반환한다.")
    @Test
    void testPaymentProcess_withValidRequest_shouldReturnApprovedResult() {
        // given
        PaymentContext paymentContext = new PaymentContext(PaymentMethodType.CREDIT_CARD,
                new CreditCardPaymentRequest(
                        10000L,
                        "1234-5678-9012-3456",
                        "12/25",
                        "123",
                        "VISA"
                )
        );

        // when
        PaymentResult result = paymentProcessor.process(paymentContext);

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
        PaymentContext paymentContext = new PaymentContext(PaymentMethodType.CREDIT_CARD,
                new CreditCardPaymentRequest(
                        -10000L,
                        "1234-5678-9012-3456",
                        "12/25",
                        "123",
                        "VISA"
                )
        );

        // when
        PaymentResult result = paymentProcessor.process(paymentContext);

        // then
        assertThat(result.status()).isEqualTo(PaymentStatus.DECLINED);
        assertThat(result.transactionId()).isNotNull();
        assertThat(result.approvedAt()).isNull();
        assertThat(result.errorCode()).isEqualTo(ErrorType.INVALID_PAYMENT_AMOUNT.getCode().name());
        assertThat(result.errorMessage()).isEqualTo(ErrorType.INVALID_PAYMENT_AMOUNT.getMessage());
    }

    @DisplayName("경계값 테스트: 결제 요청의 결제 금액이 0원인 경우 결제 성공을 반환한다.")
    @Test
    void testPaymentProcess_withZeroAmount_shouldReturnApprovedResult() {
        // given
        PaymentContext paymentContext = new PaymentContext(PaymentMethodType.CREDIT_CARD,
                new CreditCardPaymentRequest(
                        0L,
                        "1234-5678-9012-3456",
                        "12/25",
                        "123",
                        "VISA"
                )
        );

        // when
        PaymentResult result = paymentProcessor.process(paymentContext);

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
        PaymentContext paymentContext = new PaymentContext(PaymentMethodType.CREDIT_CARD,
                new CreditCardPaymentRequest(
                        10000L,
                        "invalid-card-number",
                        "12/25",
                        "123",
                        "VISA"
                )
        );

        // when
        PaymentResult result = paymentProcessor.process(paymentContext);

        // then
        assertThat(result.status()).isEqualTo(PaymentStatus.DECLINED);
        assertThat(result.transactionId()).isNotNull();
        assertThat(result.approvedAt()).isNull();
        assertThat(result.errorCode()).isEqualTo(ErrorType.INVALID_PAYMENT_CARD_NUMBER.getCode().name());
        assertThat(result.errorMessage()).isEqualTo(ErrorType.INVALID_PAYMENT_CARD_NUMBER.getMessage());
    }

    @DisplayName("잘못된 카드 유효기간으로 결제 요청 시 예외를 반환한다.")
    @Test
    void testPaymentProcess_withInvalidCardExpiryDate_shouldThrowCoreException() {
        // given
        PaymentContext paymentContext = new PaymentContext(PaymentMethodType.CREDIT_CARD,
                new CreditCardPaymentRequest(
                        10000L,
                        "1234-5678-9012-3456",
                        "invalid-expiry-date",
                        "123",
                        "VISA"
                )
        );

        // when
        PaymentResult result = paymentProcessor.process(paymentContext);

        // then
        assertThat(result.status()).isEqualTo(PaymentStatus.DECLINED);
        assertThat(result.transactionId()).isNotNull();
        assertThat(result.approvedAt()).isNull();
        assertThat(result.errorCode()).isEqualTo(ErrorType.INVALID_PAYMENT_CARD_EXPIRY.getCode().name());
        assertThat(result.errorMessage()).isEqualTo(ErrorType.INVALID_PAYMENT_CARD_EXPIRY.getMessage());
    }

    @DisplayName("잘못된 카드 CVC로 결제 요청 시 예외를 반환한다.")
    @Test
    void testPaymentProcess_withInvalidCardCvc_shouldThrowCoreException() {
        // given
        PaymentContext paymentContext = new PaymentContext(PaymentMethodType.CREDIT_CARD,
                new CreditCardPaymentRequest(
                        10000L,
                        "1234-5678-9012-3456",
                        "12/25",
                        "invalid-cvc",
                        "VISA"
                )
        );

        // when
        PaymentResult result = paymentProcessor.process(paymentContext);

        // then
        assertThat(result.status()).isEqualTo(PaymentStatus.DECLINED);
        assertThat(result.transactionId()).isNotNull();
        assertThat(result.approvedAt()).isNull();
        assertThat(result.errorCode()).isEqualTo(ErrorType.INVALID_PAYMENT_CARD_CVC.getCode().name());
        assertThat(result.errorMessage()).isEqualTo(ErrorType.INVALID_PAYMENT_CARD_CVC.getMessage());
    }
}