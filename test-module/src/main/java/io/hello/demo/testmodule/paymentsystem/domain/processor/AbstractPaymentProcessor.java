package io.hello.demo.testmodule.paymentsystem.domain.processor;

import io.hello.demo.testmodule.paymentsystem.domain.PaymentRequest;
import io.hello.demo.testmodule.paymentsystem.domain.PaymentResult;

import java.math.BigDecimal;

import static java.util.UUID.*;

public abstract class AbstractPaymentProcessor implements PaymentProcessor {

    @Override
    public PaymentResult processPayment(PaymentRequest request) {
        // 1. 결제 전 유효성 검증
        validatePaymentRequest(request);

        // 2. 결제 승인
        authenticatePayment(request);

        // 3. 결제 승인 처리
        PaymentResult result = authorizePayment(request);

        // 4. 결제 후처리
        postProcessPayment(result);

        return result;
    }

    @Override
    public PaymentResult cancelPayment(String paymentId, BigDecimal amount) {
        // 1. 취소 요청 유효성 검증
        validateCancellation(paymentId, amount);

        // 2. 취소 처리
        PaymentResult result = processCancellation(paymentId, amount);

        // 3. 취소 후처리
        postProcessCancellation(result);

        return result;
    }

    // 각 단계를 추상 메소드로 정의하여 구체 클래스에서 구현하도록 함
    protected abstract void validatePaymentRequest(PaymentRequest request);
    protected abstract void authenticatePayment(PaymentRequest request);
    protected abstract PaymentResult authorizePayment(PaymentRequest request);
    protected abstract void postProcessPayment(PaymentResult result);

    protected abstract void validateCancellation(String paymentId, BigDecimal amount);
    protected abstract PaymentResult processCancellation(String paymentId, BigDecimal amount);
    protected abstract void postProcessCancellation(PaymentResult result);

    protected String generatePaymentId() {
        // 결제 ID 생성 로직 (예: UUID)
        return "Payment:" + randomUUID().toString();
    }

    protected String generateReceiptUrl(String paymentId) {
        return "https://toss.im/receipt/" + paymentId;
    }
}
