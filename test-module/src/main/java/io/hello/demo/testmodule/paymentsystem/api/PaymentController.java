package io.hello.demo.testmodule.paymentsystem.api;

import io.hello.demo.testmodule.paymentsystem.domain.PaymentResult;
import io.hello.demo.testmodule.paymentsystem.domain.PaymentService;
import io.hello.demo.testmodule.paymentsystem.api.request.PaymentRequestDto;
import io.hello.demo.testmodule.paymentsystem.api.response.PaymentResultDto;

import java.math.BigDecimal;

public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    public PaymentResult processPayment(PaymentRequestDto request) {
        // 결제 처리 위임
        if (request == null) {
            throw new IllegalArgumentException("Payment request cannot be null");
        }
        return paymentService.processPayment(request.toPaymentRequest());
    }

    public PaymentResultDto cancelPayment(String paymentId, String paymentMethodType, BigDecimal amount) {
        // 요청 기본 검증
        if (paymentId == null || paymentId.isBlank()) {
            throw new IllegalArgumentException("Payment ID is required");
        }

        if (paymentMethodType == null || paymentMethodType.isBlank()) {
            throw new IllegalArgumentException("Payment method type is required");
        }

        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Invalid cancellation amount");
        }

        // 결제 취소 처리 위임
        PaymentResult paymentResult = paymentService.cancelPayment(paymentId, paymentMethodType, amount);
        return PaymentResultDto.of(paymentResult);
    }
}
