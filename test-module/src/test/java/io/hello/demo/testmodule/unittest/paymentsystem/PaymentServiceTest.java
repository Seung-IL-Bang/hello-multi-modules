package io.hello.demo.testmodule.unittest.paymentsystem;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static io.hello.demo.testmodule.unittest.paymentsystem.PaymentRequest.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private PgClient pgClient;

    @InjectMocks
    private PaymentService paymentService;

    @Captor
    private ArgumentCaptor<Payment> paymentCaptor;

    private PaymentRequest validPaymentRequest;
    private Payment savedPayment;
    private Payment approvedPayment;

    @BeforeEach
    void setUp() {
        // 유효한 결제 요청 데이터 설정
        validPaymentRequest = new PaymentRequest();
        validPaymentRequest.setAmount(new BigDecimal("10000"));
        validPaymentRequest.setCustomerId("customer-123");

        CardInfo cardInfo = new CardInfo();
        cardInfo.setCardNumber("1234-5678-9012-3456");
        cardInfo.setExpiryDate("12/25");
        cardInfo.setCvv("123");
        validPaymentRequest.setCardInfo(cardInfo);

        // 저장된 결제 정보 설정
        savedPayment = new Payment();
        savedPayment.setId("PAYMENT-12345678");
        savedPayment.setAmount(new BigDecimal("10000"));
        savedPayment.setStatus(PaymentStatus.CREATED);
        savedPayment.setCustomerId("customer-123");
        savedPayment.setCreatedAt(LocalDateTime.now());

        // 승인된 결제 정보 설정
        approvedPayment = new Payment();
        approvedPayment.setId("PAYMENT-12345678");
        approvedPayment.setAmount(new BigDecimal("10000"));
        approvedPayment.setStatus(PaymentStatus.APPROVED);
        approvedPayment.setCustomerId("customer-123");
        approvedPayment.setCreatedAt(LocalDateTime.now());
        approvedPayment.setPgTraceId("pg-trace-123");
        approvedPayment.setUpdatedAt(LocalDateTime.now());
    }

    @Test
    @DisplayName("정상적인 결제 처리 테스트")
    void processPayment_WithValidRequest_ShouldReturnSuccessResponse() {
        // Given
        given(paymentRepository.save(any(Payment.class)))
                .willReturn(savedPayment)
                .willReturn(approvedPayment);

        given(pgClient.requestPayment(anyString(), any(BigDecimal.class), any(CardInfo.class)))
                .willReturn("pg-trace-123");

        // When
        PaymentResponse response = paymentService.processPayment(validPaymentRequest);

        // Then
        verify(paymentRepository, times(2)).save(paymentCaptor.capture());
        Payment capturedCreatedPayment = paymentCaptor.getAllValues().get(0);
        Payment capturedApprovedPayment = paymentCaptor.getAllValues().get(1);

        assertEquals(PaymentStatus.CREATED, capturedCreatedPayment.getStatus());
        assertNull(capturedCreatedPayment.getPgTraceId());
        assertEquals(PaymentStatus.APPROVED, capturedApprovedPayment.getStatus());
        assertEquals("pg-trace-123", capturedApprovedPayment.getPgTraceId());

        assertNotNull(response);
        assertEquals(savedPayment.getId(), response.getPaymentId());
        assertEquals(savedPayment.getAmount(), response.getAmount());
        assertEquals(PaymentStatus.APPROVED, response.getStatus());
        assertNotNull(response.getApprovedAt());
    }

    @Test
    @DisplayName("결제 금액이 0 이하인 경우 예외 발생 테스트")
    void processPayment_WithNonPositiveAmount_ShouldThrowException() {
        // Given
        PaymentRequest invalidRequest = new PaymentRequest();
        invalidRequest.setAmount(BigDecimal.ZERO);
        invalidRequest.setCustomerId("customer-123");
        invalidRequest.setCardInfo(validPaymentRequest.getCardInfo());

        // When & Then
        InvalidPaymentException exception = assertThrows(
                InvalidPaymentException.class,
                () -> paymentService.processPayment(invalidRequest)
        );

        assertTrue(exception.getMessage().contains("Payment amount must be positive"));
        verify(paymentRepository, never()).save(any(Payment.class));
        verify(pgClient, never()).requestPayment(anyString(), any(BigDecimal.class), any(PaymentRequest.CardInfo.class));
    }

    @Test
    @DisplayName("카드 정보가 없는 경우 예외 발생 테스트")
    void processPayment_WithNoCardInfo_ShouldThrowException() {
        // Given
        PaymentRequest invalidRequest = new PaymentRequest();
        invalidRequest.setAmount(new BigDecimal("10000"));
        invalidRequest.setCustomerId("customer-123");
        invalidRequest.setCardInfo(null);

        // When & Then
        InvalidPaymentException exception = assertThrows(
                InvalidPaymentException.class,
                () -> paymentService.processPayment(invalidRequest)
        );

        assertTrue(exception.getMessage().contains("Card information is required"));
        verify(paymentRepository, never()).save(any(Payment.class));
        verify(pgClient, never()).requestPayment(anyString(), any(BigDecimal.class), any(PaymentRequest.CardInfo.class));
    }

    @Test
    @DisplayName("PG사 결제 요청 실패 테스트")
    void processPayment_WhenPgClientFails_ShouldThrowException() {
        // Given
        given(paymentRepository.save(any(Payment.class)))
                .willReturn(savedPayment);

        given(pgClient.requestPayment(anyString(), any(BigDecimal.class), any(PaymentRequest.CardInfo.class)))
                .willThrow(new RuntimeException("PG client error"));

        // When & Then
        InvalidPaymentException exception = assertThrows(
                InvalidPaymentException.class,
                () -> paymentService.processPayment(validPaymentRequest)
        );

        assertEquals("Payment processing failed: " + "PG client error", exception.getMessage());

        verify(paymentRepository, times(2)).save(paymentCaptor.capture());
        Payment capturedCreatedPayment = paymentCaptor.getAllValues().get(0);
        Payment capturedFailedPayment = paymentCaptor.getAllValues().get(1);

        assertEquals(PaymentStatus.CREATED, capturedCreatedPayment.getStatus());
        assertEquals(PaymentStatus.FAILED, capturedFailedPayment.getStatus());
        assertNotNull(capturedFailedPayment.getFailReason());
    }

    @Test
    @DisplayName("정상적인 결제 취소 테스트")
    void cancelPayment_WithValidRequest_ShouldReturnSuccessResponse() {
        // Given
        BigDecimal cancelAmount = new BigDecimal("10000");

        given(paymentRepository.findById("PAYMENT-12345678"))
                .willReturn(Optional.of(approvedPayment));

        given(pgClient.cancelPayment(anyString(), anyString(), any(BigDecimal.class)))
                .willReturn("pg-cancel-123");

        Payment cancelledPayment = new Payment();
        cancelledPayment.setId("PAYMENT-12345678");
        cancelledPayment.setAmount(new BigDecimal("10000"));
        cancelledPayment.setStatus(PaymentStatus.CANCELLED);
        cancelledPayment.setCustomerId("customer-123");
        cancelledPayment.setPgTraceId("pg-trace-123");
        cancelledPayment.setPgCancelId("pg-cancel-123");
        cancelledPayment.setCancelledAmount(cancelAmount);
        cancelledPayment.setUpdatedAt(LocalDateTime.now());

        given(paymentRepository.save(any(Payment.class)))
                .willReturn(cancelledPayment);

        // When
        PaymentResponse response = paymentService.cancelPayment("PAYMENT-12345678", cancelAmount);

        // Then
        verify(paymentRepository).findById("PAYMENT-12345678");
        verify(pgClient).cancelPayment("PAYMENT-12345678", "pg-trace-123", cancelAmount);
        verify(paymentRepository).save(paymentCaptor.capture());

        Payment capturedPayment = paymentCaptor.getValue();
        assertEquals(PaymentStatus.CANCELLED, capturedPayment.getStatus());
        assertEquals("pg-cancel-123", capturedPayment.getPgCancelId());
        assertEquals(cancelAmount, capturedPayment.getCancelledAmount());

        assertNotNull(response);
        assertEquals("PAYMENT-12345678", response.getPaymentId());
        assertEquals(PaymentStatus.CANCELLED, response.getStatus());
    }

    @Test
    @DisplayName("존재하지 않는 결제 취소 시도 테스트")
    void cancelPayment_WithNonExistingPaymentId_ShouldThrowException() {
        // Given
        given(paymentRepository.findById("NON-EXISTING-ID"))
                .willReturn(Optional.empty());

        // When & Then
        PaymentNotFoundException exception = assertThrows(
                PaymentNotFoundException.class,
                () -> paymentService.cancelPayment("NON-EXISTING-ID", new BigDecimal("10000"))
        );

        assertTrue(exception.getMessage().contains("Payment not found"));

        verify(paymentRepository).findById("NON-EXISTING-ID");
        verify(pgClient, never()).cancelPayment(anyString(), anyString(), any(BigDecimal.class));
        verify(paymentRepository, never()).save(any(Payment.class));
    }

    @Test
    @DisplayName("취소 불가능한 상태의 결제 취소 시도 테스트")
    void cancelPayment_WithNonCancellablePayment_ShouldThrowException() {
        // Given
        Payment failedPayment = new Payment();
        failedPayment.setId("PAYMENT-12345678");
        failedPayment.setAmount(new BigDecimal("10000"));
        failedPayment.setStatus(PaymentStatus.FAILED);
        failedPayment.setCustomerId("customer-123");

        given(paymentRepository.findById("PAYMENT-12345678"))
                .willReturn(Optional.of(failedPayment));

        // When & Then
        InvalidPaymentException exception = assertThrows(
                InvalidPaymentException.class,
                () -> paymentService.cancelPayment("PAYMENT-12345678", new BigDecimal("10000"))
        );

        assertTrue(exception.getMessage().contains("Payment cannot be cancelled"));

        verify(paymentRepository).findById("PAYMENT-12345678");
        verify(pgClient, never()).cancelPayment(anyString(), anyString(), any(BigDecimal.class));
        verify(paymentRepository, never()).save(any(Payment.class));
    }

    @Test
    @DisplayName("결제 금액보다 큰 금액 취소 시도 테스트")
    void cancelPayment_WithExcessiveAmount_ShouldThrowException() {
        // Given
        BigDecimal excessiveAmount = new BigDecimal("20000");

        given(paymentRepository.findById("PAYMENT-12345678"))
                .willReturn(Optional.of(approvedPayment));

        // When & Then
        InvalidPaymentException exception = assertThrows(
                InvalidPaymentException.class,
                () -> paymentService.cancelPayment("PAYMENT-12345678", excessiveAmount)
        );

        assertTrue(exception.getMessage().contains("Cancel amount cannot exceed payment amount"));

        verify(paymentRepository).findById("PAYMENT-12345678");
        verify(pgClient, never()).cancelPayment(anyString(), anyString(), any(BigDecimal.class));
        verify(paymentRepository, never()).save(any(Payment.class));
    }

    @Test
    @DisplayName("PG사 취소 요청 실패 테스트")
    void cancelPayment_WhenPgClientFails_ShouldThrowException() {
        // Given
        BigDecimal cancelAmount = new BigDecimal("10000");

        given(paymentRepository.findById("PAYMENT-12345678"))
                .willReturn(Optional.of(approvedPayment));

        given(pgClient.cancelPayment(anyString(), anyString(), any(BigDecimal.class)))
                .willThrow(new RuntimeException("PG client cancel error"));

        // When & Then
        InvalidPaymentException exception = assertThrows(
                InvalidPaymentException.class,
                () -> paymentService.cancelPayment("PAYMENT-12345678", cancelAmount)
        );

        assertTrue(exception.getMessage().contains("Payment cancellation failed"));

        verify(paymentRepository).findById("PAYMENT-12345678");
        verify(pgClient).cancelPayment("PAYMENT-12345678", "pg-trace-123", cancelAmount);
        verify(paymentRepository, never()).save(any(Payment.class));
    }

}