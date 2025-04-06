package io.hello.demo.testmodule.unittest.paymentstatus;

import io.hello.demo.testmodule.unittest.simplesystem.paymentstatus.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private PointService pointService;

    @Mock
    private RefundService refundService;

    @InjectMocks
    private PaymentService paymentStatusService;

    private Payment samplePayment;

    @BeforeEach
    void setUp() {
        // 테스트 전 샘플 데이터 초기화
        samplePayment = new Payment("user123", 10000L, "PENDING");
        samplePayment.setStatus("PENDING"); // 상태 초기화
    }


    // 테스트 1: "SUCCESS" 상태로 전이 시 포인트 적립 호출 확인
    @Test
    void testProcessPayment_SuccessState_CallsAddPoints() {
        // Given
        when(paymentRepository.findById(1L)).thenReturn(Optional.of(samplePayment));
        when(paymentRepository.save(any(Payment.class))).thenReturn(samplePayment);

        // When
        paymentStatusService.processPayment(1L, "SUCCESS");

        // Then
        assertEquals("SUCCESS", samplePayment.getStatus());
        verify(pointService, times(1)).addPoints("user123", 1000L); // 10000 / 10
        verify(refundService, never()).processRefund(anyLong(), anyLong());
        verify(paymentRepository, times(1)).save(samplePayment);
    }

    // 테스트 2: "CANCELLED" 상태로 전이 시 환불 호출 확인
    @Test
    void testProcessPayment_CancelledState_CallsRefund() {
        // Given
        when(paymentRepository.findById(1L)).thenReturn(Optional.of(samplePayment));
        when(paymentRepository.save(any(Payment.class))).thenReturn(samplePayment);

        // When
        paymentStatusService.processPayment(1L, "CANCELLED");

        // Then
        assertEquals("CANCELLED", samplePayment.getStatus());
        verify(refundService, times(1)).processRefund(samplePayment.getId(), 10000L);
        verify(pointService, never()).addPoints(anyString(), anyLong());
        verify(paymentRepository, times(1)).save(samplePayment);
    }

    // 테스트 3: 존재하지 않는 결제 ID로 호출 시 예외 발생 확인
    @Test
    void testProcessPayment_NonExistentPayment_ThrowsException() {
        // Given
        when(paymentRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            paymentStatusService.processPayment(999L, "SUCCESS");
        });
        assertEquals("Payment not found", exception.getMessage());
        verify(pointService, never()).addPoints(anyString(), anyLong());
        verify(refundService, never()).processRefund(anyLong(), anyLong());
        verify(paymentRepository, never()).save(any(Payment.class));
    }

    // 테스트 4: 유효하지 않은 상태로 호출 시 예외 발생 확인
    @Test
    void testProcessPayment_InvalidStatus_ThrowsException() {
        // Given
        when(paymentRepository.findById(1L)).thenReturn(Optional.of(samplePayment));

        // When & Then
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            paymentStatusService.processPayment(1L, "INVALID");
        });
        assertEquals("Invalid status: INVALID", exception.getMessage());
        assertEquals("PENDING", samplePayment.getStatus()); // 상태 변경 없음
        verify(pointService, never()).addPoints(anyString(), anyLong());
        verify(refundService, never()).processRefund(anyLong(), anyLong());
        verify(paymentRepository, never()).save(any(Payment.class));
    }


}