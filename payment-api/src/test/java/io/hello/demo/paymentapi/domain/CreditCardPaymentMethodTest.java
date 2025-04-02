package io.hello.demo.paymentapi.domain;

import io.hello.demo.paymentapi.domain.generator.TransactionIdGenerator;
import io.hello.demo.paymentapi.domain.generator.UuidTransactionIdGenerator;
import io.hello.demo.paymentapi.domain.method.*;
import io.hello.demo.paymentapi.domain.method.validator.virtualaccount.VirtualAccountAmountValidator;
import io.hello.demo.paymentapi.domain.method.validator.virtualaccount.VirtualAccountBankCodeValidator;
import io.hello.demo.paymentapi.domain.method.validator.virtualaccount.VirtualAccountHolderNameValidator;
import io.hello.demo.paymentapi.domain.method.validator.virtualaccount.VirtualAccountNumberValidator;
import io.hello.demo.paymentapi.domain.processor.DefaultPaymentProcessor;
import io.hello.demo.paymentapi.domain.processor.PaymentProcessor;
import io.hello.demo.paymentapi.domain.request.CreditCardPaymentRequest;
import io.hello.demo.paymentapi.domain.method.validator.PaymentMethodValidatorFactory;
import io.hello.demo.paymentapi.domain.method.validator.creditcard.CardAmountValidator;
import io.hello.demo.paymentapi.domain.method.validator.creditcard.CardCvcValidator;
import io.hello.demo.paymentapi.domain.method.validator.creditcard.CardExpiryValidator;
import io.hello.demo.paymentapi.domain.method.validator.creditcard.CardNumberValidator;
import io.hello.demo.paymentapi.support.error.ErrorType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


class CreditCardPaymentMethodTest {

    private PaymentProcessor paymentProcessor;
    private TransactionIdGenerator transactionIdGenerator;

    @BeforeEach
    void setUp() {
        transactionIdGenerator = new UuidTransactionIdGenerator();
        PaymentMethodValidatorFactory creditCardMethodValidatorFactory = new PaymentMethodValidatorFactory(List.of(
                new CardNumberValidator(),
                new CardCvcValidator(),
                new CardExpiryValidator(),
                new CardAmountValidator()
        ));

        List<PaymentMethod> paymentMethods = List.of(
                new CreditCardPaymentMethod(creditCardMethodValidatorFactory)
        );

        PaymentMethodFactory paymentMethodFactory = new PaymentMethodFactory(paymentMethods);
        paymentProcessor = new DefaultPaymentProcessor(paymentMethodFactory);
    }

    @DisplayName("[신용카드] 신용 카드의 올바른 결제 요청에 대해 결제 성공을 반환한다.")
    @Test
    void testPaymentProcess_withValidRequest_shouldReturnApprovedResult() {
        // given
        String transactionId = transactionIdGenerator.generate();
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
        PaymentResult result = paymentProcessor.process(paymentContext, transactionId);

        // then
        assertThat(result.status()).isEqualTo(PaymentStatus.APPROVED);
        assertThat(result.transactionId()).isNotNull();
        assertThat(result.approvedAt()).isNotNull();
        assertThat(result.errorMessage()).isNull();
        assertThat(result.errorCode()).isNull();
    }

    @DisplayName("[신용카드] 신용 카드의 잘못 된 결제 요청에 대해 결제 실패를 반환한다.")
    @Test
    void testPaymentProcess_withInvalidRequest_shouldReturnDeclinedResult() {
        // given
        String transactionId = transactionIdGenerator.generate();
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
        PaymentResult result = paymentProcessor.process(paymentContext, transactionId);

        // then
        assertThat(result.status()).isEqualTo(PaymentStatus.DECLINED);
        assertThat(result.transactionId()).isNotNull();
        assertThat(result.approvedAt()).isNull();
        assertThat(result.errorCode()).isEqualTo(ErrorType.INVALID_PAYMENT_CARD_NUMBER.getCode().name());
        assertThat(result.errorMessage()).isEqualTo(ErrorType.INVALID_PAYMENT_CARD_NUMBER.getMessage());
    }

    @DisplayName("[신용카드] 결제 요청의 결제 금액이 0원 미만인 경우 결제 실패를 반환한다.")
    @Test
    void testPaymentProcess_withInvalidAmount_shouldThrowCoreException() {
        // given
        String transactionId = transactionIdGenerator.generate();
        PaymentContext paymentContext = new PaymentContext(PaymentMethodType.CREDIT_CARD,
                new CreditCardPaymentRequest(0L, "", "", "", "")
        );

        // when
        PaymentResult result = paymentProcessor.process(paymentContext, transactionId);

        // then
        assertThat(result.status()).isEqualTo(PaymentStatus.DECLINED);
        assertThat(result.transactionId()).isNotNull();
        assertThat(result.approvedAt()).isNull();
    }

    @DisplayName("[신용카드] 경계값 테스트: 결제 요청의 결제 금액이 0원인 경우 결제 성공을 반환한다.")
    @Test
    void testPaymentProcess_withZeroAmount_shouldReturnApprovedResult() {
        // given
        String transactionId = transactionIdGenerator.generate();
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
        PaymentResult result = paymentProcessor.process(paymentContext, transactionId);

        // then
        assertThat(result.status()).isEqualTo(PaymentStatus.APPROVED);
        assertThat(result.transactionId()).isNotNull();
        assertThat(result.approvedAt()).isNotNull();
        assertThat(result.errorMessage()).isNull();
        assertThat(result.errorCode()).isNull();
    }

    @DisplayName("[신용카드] 잘못된 카드 번호로 결제 요청 시 결제 실패를 반환한다.")
    @Test
    void testPaymentProcess_withInvalidCardNumber_shouldThrowCoreException() {
        // given
        String transactionId = transactionIdGenerator.generate();
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
        PaymentResult result = paymentProcessor.process(paymentContext, transactionId);

        // then
        assertThat(result.status()).isEqualTo(PaymentStatus.DECLINED);
        assertThat(result.transactionId()).isNotNull();
        assertThat(result.approvedAt()).isNull();
        assertThat(result.errorCode()).isEqualTo(ErrorType.INVALID_PAYMENT_CARD_NUMBER.getCode().name());
        assertThat(result.errorMessage()).isEqualTo(ErrorType.INVALID_PAYMENT_CARD_NUMBER.getMessage());
    }

    @DisplayName("[신용카드] 잘못된 카드 유효기간으로 결제 요청 시 결제 실패를 반환한다.")
    @Test
    void testPaymentProcess_withInvalidCardExpiryDate_shouldThrowCoreException() {
        // given
        String transactionId = transactionIdGenerator.generate();
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
        PaymentResult result = paymentProcessor.process(paymentContext, transactionId);

        // then
        assertThat(result.status()).isEqualTo(PaymentStatus.DECLINED);
        assertThat(result.transactionId()).isNotNull();
        assertThat(result.approvedAt()).isNull();
        assertThat(result.errorCode()).isEqualTo(ErrorType.INVALID_PAYMENT_CARD_EXPIRY.getCode().name());
        assertThat(result.errorMessage()).isEqualTo(ErrorType.INVALID_PAYMENT_CARD_EXPIRY.getMessage());
    }

    @DisplayName("[신용카드] 잘못된 카드 CVC로 결제 요청 시 결제 실패를 반환한다.")
    @Test
    void testPaymentProcess_withInvalidCardCvc_shouldThrowCoreException() {
        // given
        String transactionId = transactionIdGenerator.generate();
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
        PaymentResult result = paymentProcessor.process(paymentContext, transactionId);

        // then
        assertThat(result.status()).isEqualTo(PaymentStatus.DECLINED);
        assertThat(result.transactionId()).isNotNull();
        assertThat(result.approvedAt()).isNull();
        assertThat(result.errorCode()).isEqualTo(ErrorType.INVALID_PAYMENT_CARD_CVC.getCode().name());
        assertThat(result.errorMessage()).isEqualTo(ErrorType.INVALID_PAYMENT_CARD_CVC.getMessage());
    }
}