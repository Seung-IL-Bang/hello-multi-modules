package io.hello.demo.testmodule.paymentsystem.domain;

import io.hello.demo.testmodule.paymentsystem.PaymentRequestFixture;
import io.hello.demo.testmodule.paymentsystem.domain.processor.AccountTransferPaymentProcessor;
import io.hello.demo.testmodule.paymentsystem.domain.processor.CardPaymentProcessor;
import io.hello.demo.testmodule.paymentsystem.domain.processor.TossPayPaymentProcessor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

class PaymentProcessorTest {

    @DisplayName("카드 처리기 결제 테스트")
    @Test
    void testProcessPaymentWithCard() {
        // Arrange
        CardPaymentProcessor processor = new CardPaymentProcessor();
        PaymentRequest paymentRequest = PaymentRequestFixture.createPaymentRequest();

        // Act
        PaymentResult paymentResult = processor.processPayment(paymentRequest);

        // Assert
        assertThat(paymentResult).isNotNull();
        assertThat(paymentResult.getPaymentId()).isNotNull();
        assertThat(paymentResult.getPaymentId()).contains("Payment:");
        assertThat(paymentResult.getStatus()).isEqualTo("APPROVED");
        assertThat(paymentResult.getApprovedAmount()).isEqualTo(new BigDecimal("100.00"));
        assertThat(paymentResult.getApprovedAt()).isNotNull();
        assertThat(paymentResult.getPaymentMethodType()).isEqualTo("CARD");
        assertThat(paymentResult.getReceiptUrl()).isNotNull();
        assertThat(paymentResult.getReceiptUrl()).contains("https://toss.im/receipt/");
    }

    @DisplayName("카드 처리기 결제 취소 테스트")
    @Test
    void testCancelPaymentWithCard() {
        // Arrange
        CardPaymentProcessor processor = new CardPaymentProcessor();
        PaymentRequest paymentRequest = PaymentRequestFixture.createPaymentRequest();
        PaymentResult paymentResult = processor.processPayment(paymentRequest);

        // Act
        PaymentResult cancelResult = processor.cancelPayment(paymentResult.getPaymentId(), paymentResult.getApprovedAmount());

        // Assert
        assertThat(cancelResult).isNotNull();
        assertThat(cancelResult.getPaymentId()).isEqualTo(paymentResult.getPaymentId());
        assertThat(cancelResult.getPaymentId()).isNotNull();
        assertThat(cancelResult.getPaymentId()).contains("Payment:");
        assertThat(cancelResult.getStatus()).isEqualTo("CANCELLED");
        assertThat(cancelResult.getApprovedAmount()).isEqualTo(new BigDecimal("-100.00"));
        assertThat(cancelResult.getApprovedAt()).isNotNull();
        assertThat(cancelResult.getPaymentMethodType()).isEqualTo("CARD");
        assertThat(cancelResult.getReceiptUrl()).isNotNull();
        assertThat(cancelResult.getReceiptUrl()).contains("https://toss.im/receipt/");
    }

    @DisplayName("계좌이체 처리기 결제 테스트")
    @Test
    void testProcessPaymentWithAccountTransfer() {
        // Arrange
        PaymentRequest paymentRequest = PaymentRequestFixture.createPaymentRequest();
        AccountTransferPaymentProcessor processor = new AccountTransferPaymentProcessor();

        // Act
        PaymentResult paymentResult = processor.processPayment(paymentRequest);

        // Assert
        assertThat(paymentResult).isNotNull();
        assertThat(paymentResult.getPaymentId()).isNotNull();
        assertThat(paymentResult.getPaymentId()).contains("Payment:");
        assertThat(paymentResult.getStatus()).isEqualTo("APPROVED");
        assertThat(paymentResult.getApprovedAmount()).isEqualTo(new BigDecimal("100.00"));
        assertThat(paymentResult.getApprovedAt()).isNotNull();
        assertThat(paymentResult.getPaymentMethodType()).isEqualTo("ACCOUNT_TRANSFER");
        assertThat(paymentResult.getReceiptUrl()).isNotNull();
        assertThat(paymentResult.getReceiptUrl()).contains("https://toss.im/receipt/");
    }

    @DisplayName("계좌이체 처리기 결제 취소 테스트")
    @Test
    void testCancelPaymentWithAccountTransfer() {
        // Arrange
        AccountTransferPaymentProcessor processor = new AccountTransferPaymentProcessor();
        PaymentRequest paymentRequest = PaymentRequestFixture.createPaymentRequest();
        PaymentResult paymentResult = processor.processPayment(paymentRequest);

        // Act
        PaymentResult cancelResult = processor.cancelPayment(paymentResult.getPaymentId(), paymentResult.getApprovedAmount());

        // Assert
        assertThat(cancelResult).isNotNull();
        assertThat(cancelResult.getPaymentId()).isEqualTo(paymentResult.getPaymentId());
        assertThat(cancelResult.getPaymentId()).isNotNull();
        assertThat(cancelResult.getPaymentId()).contains("Payment:");
        assertThat(cancelResult.getStatus()).isEqualTo("CANCELLED");
        assertThat(cancelResult.getApprovedAmount()).isEqualTo(new BigDecimal("-100.00"));
        assertThat(cancelResult.getApprovedAt()).isNotNull();
        assertThat(cancelResult.getPaymentMethodType()).isEqualTo("ACCOUNT_TRANSFER");
        assertThat(cancelResult.getReceiptUrl()).isNotNull();
        assertThat(cancelResult.getReceiptUrl()).contains("https://toss.im/receipt/");
    }


    @DisplayName("토스페이 결제기 결제 테스트")
    @Test
    void testProcessPaymentWithTossPay() {
        // Arrange
        PaymentRequest paymentRequest = PaymentRequestFixture.createPaymentRequest();
        TossPayPaymentProcessor processor = new TossPayPaymentProcessor();

        // Act
        PaymentResult paymentResult = processor.processPayment(paymentRequest);

        // Assert
        assertThat(paymentResult).isNotNull();
        assertThat(paymentResult.getPaymentId()).isNotNull();
        assertThat(paymentResult.getPaymentId()).contains("Payment:");
        assertThat(paymentResult.getStatus()).isEqualTo("APPROVED");
        assertThat(paymentResult.getApprovedAmount()).isEqualTo(new BigDecimal("100.00"));
        assertThat(paymentResult.getApprovedAt()).isNotNull();
        assertThat(paymentResult.getPaymentMethodType()).isEqualTo("TOSS_PAY");
        assertThat(paymentResult.getReceiptUrl()).isNotNull();
        assertThat(paymentResult.getReceiptUrl()).contains("https://toss.im/receipt/");
    }

    @DisplayName("토스페이 결제기 결제 취소 테스트")
    @Test
    void testCancelPaymentWithTossPay() {
        // Arrange
        TossPayPaymentProcessor processor = new TossPayPaymentProcessor();
        PaymentRequest paymentRequest = PaymentRequestFixture.createPaymentRequest();
        PaymentResult paymentResult = processor.processPayment(paymentRequest);

        // Act
        PaymentResult cancelResult = processor.cancelPayment(paymentResult.getPaymentId(), paymentResult.getApprovedAmount());

        // Assert
        assertThat(cancelResult).isNotNull();
        assertThat(cancelResult.getPaymentId()).isEqualTo(paymentResult.getPaymentId());
        assertThat(cancelResult.getPaymentId()).isNotNull();
        assertThat(cancelResult.getPaymentId()).contains("Payment:");
        assertThat(cancelResult.getStatus()).isEqualTo("CANCELLED");
        assertThat(cancelResult.getApprovedAmount()).isEqualTo(new BigDecimal("-100.00"));
        assertThat(cancelResult.getApprovedAt()).isNotNull();
        assertThat(cancelResult.getPaymentMethodType()).isEqualTo("TOSS_PAY");
        assertThat(cancelResult.getReceiptUrl()).isNotNull();
        assertThat(cancelResult.getReceiptUrl()).contains("https://toss.im/receipt/");
    }
}