package io.hello.demo.testmodule.domain;

import io.hello.demo.testmodule.PaymentRequestFixture;
import io.hello.demo.testmodule.paymentsystem.domain.PaymentRequest;
import io.hello.demo.testmodule.paymentsystem.domain.PaymentResult;
import io.hello.demo.testmodule.paymentsystem.domain.PaymentService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

class PaymentServiceTest {

    @DisplayName("카드 결제 서비스 테스트")
    @Test
    void testPaymentService() {
        // Arrange
        PaymentService paymentService = new PaymentService();
        PaymentRequest paymentRequest = PaymentRequestFixture.createPaymentRequest("CARD");

        // Act
        PaymentResult paymentResult = paymentService.processPayment(paymentRequest);

        // Assert
        assertThat(paymentResult).isNotNull();
        assertThat(paymentResult.getPaymentId()).isNotNull();
        assertThat(paymentResult.getPaymentId()).contains("Payment:");
        assertThat(paymentResult.getStatus()).isEqualTo("APPROVED");
        assertThat(paymentResult.getApprovedAmount()).isEqualTo(paymentRequest.getAmount());
        assertThat(paymentResult.getApprovedAt()).isNotNull();
        assertThat(paymentResult.getPaymentMethodType()).isEqualTo("CARD");
        assertThat(paymentResult.getReceiptUrl()).isNotNull();
        assertThat(paymentResult.getReceiptUrl()).contains("https://toss.im/receipt/");
    }

    @DisplayName("카드 결제 서비스 취소 테스트")
    @Test
    void testCancelPaymentService() {
        // Arrange
        PaymentService paymentService = new PaymentService();
        PaymentRequest paymentRequest = PaymentRequestFixture.createPaymentRequest("CARD");
        PaymentResult paymentResult = paymentService.processPayment(paymentRequest);

        // Act
        PaymentResult cancelResult = paymentService.cancelPayment(paymentResult.getPaymentId(), "CARD", paymentResult.getApprovedAmount());

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

    @DisplayName("계좌이체 결제 서비스 테스트")
    @Test
    void testAccountTransferPaymentService() {
        // Arrange
        PaymentService paymentService = new PaymentService();
        PaymentRequest paymentRequest = PaymentRequestFixture.createPaymentRequest("ACCOUNT_TRANSFER");

        // Act
        PaymentResult paymentResult = paymentService.processPayment(paymentRequest);

        // Assert
        assertThat(paymentResult).isNotNull();
        assertThat(paymentResult.getPaymentId()).isNotNull();
        assertThat(paymentResult.getPaymentId()).contains("Payment:");
        assertThat(paymentResult.getStatus()).isEqualTo("APPROVED");
        assertThat(paymentResult.getApprovedAmount()).isEqualTo(paymentRequest.getAmount());
        assertThat(paymentResult.getApprovedAt()).isNotNull();
        assertThat(paymentResult.getPaymentMethodType()).isEqualTo("ACCOUNT_TRANSFER");
        assertThat(paymentResult.getReceiptUrl()).isNotNull();
        assertThat(paymentResult.getReceiptUrl()).contains("https://toss.im/receipt/");
    }

    @DisplayName("계좌이체 결제 서비스 취소 테스트")
    @Test
    void testCancelAccountTransferPaymentService() {
        // Arrange
        PaymentService paymentService = new PaymentService();
        PaymentRequest paymentRequest = PaymentRequestFixture.createPaymentRequest("ACCOUNT_TRANSFER");
        PaymentResult paymentResult = paymentService.processPayment(paymentRequest);

        // Act
        PaymentResult cancelResult = paymentService.cancelPayment(paymentResult.getPaymentId(), "ACCOUNT_TRANSFER", paymentResult.getApprovedAmount());

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

    @DisplayName("토스페이 결제 서비스 테스트")
    @Test
    void testTossPayPaymentService() {
        // Arrange
        PaymentService paymentService = new PaymentService();
        PaymentRequest paymentRequest = PaymentRequestFixture.createPaymentRequest("TOSS_PAY");

        // Act
        PaymentResult paymentResult = paymentService.processPayment(paymentRequest);

        // Assert
        assertThat(paymentResult).isNotNull();
        assertThat(paymentResult.getPaymentId()).isNotNull();
        assertThat(paymentResult.getPaymentId()).contains("Payment:");
        assertThat(paymentResult.getStatus()).isEqualTo("APPROVED");
        assertThat(paymentResult.getApprovedAmount()).isEqualTo(paymentRequest.getAmount());
        assertThat(paymentResult.getApprovedAt()).isNotNull();
        assertThat(paymentResult.getPaymentMethodType()).isEqualTo("TOSS_PAY");
        assertThat(paymentResult.getReceiptUrl()).isNotNull();
        assertThat(paymentResult.getReceiptUrl()).contains("https://toss.im/receipt/");
    }

    @DisplayName("토스페이 결제 서비스 취소 테스트")
    @Test
    void testCancelTossPayPaymentService() {
        // Arrange
        PaymentService paymentService = new PaymentService();
        PaymentRequest paymentRequest = PaymentRequestFixture.createPaymentRequest("TOSS_PAY");
        PaymentResult paymentResult = paymentService.processPayment(paymentRequest);

        // Act
        PaymentResult cancelResult = paymentService.cancelPayment(paymentResult.getPaymentId(), "TOSS_PAY", paymentResult.getApprovedAmount());

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