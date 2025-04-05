package io.hello.demo.testmodule.domain.processor;

import io.hello.demo.testmodule.domain.PaymentRequest;
import io.hello.demo.testmodule.domain.PaymentResult;
import io.hello.demo.testmodule.domain.TossPayInfo;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class TossPayPaymentProcessor extends AbstractPaymentProcessor {
    @Override
    protected void validatePaymentRequest(PaymentRequest request) {
        if (request.getTossPayInfo() == null) {
            throw new IllegalArgumentException("TossPay information is required");
        }

        TossPayInfo tossPayInfo = request.getTossPayInfo();
        if (tossPayInfo.getTossPayToken() == null || tossPayInfo.getTossPayToken().isBlank()) {
            throw new IllegalArgumentException("TossPay token is required");
        }
    }

    @Override
    protected void authenticatePayment(PaymentRequest request) {
        // TossPay 인증 절차
        // 실제로는 TossPay API와 연동하여 인증 처리
        TossPayInfo tossPayInfo = request.getTossPayInfo();
        System.out.println("Authenticating TossPay with token: " + maskToken(tossPayInfo.getTossPayToken()));
    }

    @Override
    protected PaymentResult authorizePayment(PaymentRequest request) {
        // 토스페이 승인 절차
        // 실제로는 토스페이 시스템과 연동하여 승인 처리
        String paymentId = generatePaymentId();

        System.out.println("Authorizing TossPay payment for order: " + request.getOrderId() +
                " with amount: " + request.getAmount());

        return new PaymentResult.Builder()
                .paymentId(paymentId)
                .status("APPROVED")
                .approvedAmount(request.getAmount())
                .approvedAt(LocalDateTime.now())
                .paymentMethodType("TOSS_PAY")
                .receiptUrl(generateReceiptUrl(paymentId))
                .build();
    }

    @Override
    protected void postProcessPayment(PaymentResult result) {
        // 토스페이 후처리
        // 예: 결제 내역 저장, 알림 발송 등
        System.out.println("Post-processing TossPay payment: " + result.getPaymentId());
    }

    @Override
    protected void validateCancellation(String paymentId, BigDecimal amount) {
        if (paymentId == null || paymentId.isEmpty()) {
            throw new IllegalArgumentException("Payment ID is required");
        }

        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Valid amount is required");
        }

        // 실제로는 DB에서 결제 정보를 조회하여 취소 가능 여부 확인
        System.out.println("Validating cancellation for payment: " + paymentId);
    }

    @Override
    protected PaymentResult processCancellation(String paymentId, BigDecimal amount) {
        // 토스페이 취소 처리
        // 실제로는 토스페이 시스템과 연동하여 취소 처리
        System.out.println("Processing cancellation for payment: " + paymentId +
                " with amount: " + amount);

        return new PaymentResult.Builder()
                .paymentId(paymentId)
                .status("CANCELLED")
                .approvedAmount(amount.negate())
                .approvedAt(LocalDateTime.now())
                .paymentMethodType("TOSS_PAY")
                .receiptUrl(generateReceiptUrl(paymentId))
                .build();
    }

    @Override
    protected void postProcessCancellation(PaymentResult result) {
        // 토스페이 취소 후처리
        // 예: 취소 내역 저장, 알림 발송 등
        System.out.println("Post-processing TossPay cancellation: " + result.getPaymentId());
    }

    // 토큰 마스킹 유틸리티 메소드
    private String maskToken(String token) {
        if (token.length() < 10) {
            return token;
        }
        return token.substring(0, 4) + "****" + token.substring(token.length() - 4);
    }
}
