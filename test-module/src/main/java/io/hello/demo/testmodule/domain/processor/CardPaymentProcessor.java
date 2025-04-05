package io.hello.demo.testmodule.domain.processor;

import io.hello.demo.testmodule.domain.CardInfo;
import io.hello.demo.testmodule.domain.PaymentRequest;
import io.hello.demo.testmodule.domain.PaymentResult;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class CardPaymentProcessor extends AbstractPaymentProcessor {

    // ==== 결제 처리 관련 메소드 === //
    @Override
    protected void validatePaymentRequest(PaymentRequest request) {
        if (request.getCardInfo() == null) {
            throw new IllegalArgumentException("Card information is required");
        }

        CardInfo cardInfo = request.getCardInfo();
        if (cardInfo.getCardNumber() == null || cardInfo.getCardNumber().isBlank()) {
            throw new IllegalArgumentException("Card number is required");
        }

        if (cardInfo.getExpiryDate() == null || cardInfo.getExpiryDate().isBlank()) {
            throw new IllegalArgumentException("Expiry date is required");
        }

        if (cardInfo.getCvv() == null || cardInfo.getCvv().isBlank()) {
            throw new IllegalArgumentException("CVV is required");
        }
    }

    @Override
    protected void authenticatePayment(PaymentRequest request) {
        // 카드 결제 인증 절차
        // 실제로는 카드사 시스템과 연동하여 인증 처리
        CardInfo cardInfo = request.getCardInfo();
        System.out.println("Authenticating card payment with card number: " +
                maskCardNumber(cardInfo.getCardNumber()));
    }

    @Override
    protected PaymentResult authorizePayment(PaymentRequest request) {
        // 카드 결제 승인 절차
        // 실제로는 카드사 시스템과 연동하여 승인 처리
        String paymentId = generatePaymentId();

        System.out.println("Authorizing card payment for order: " + request.getOrderId() +
                " with amount: " + request.getAmount());

        return new PaymentResult.Builder()
                .paymentId(paymentId)
                .status("APPROVED")
                .approvedAmount(request.getAmount())
                .approvedAt(LocalDateTime.now())
                .paymentMethodType("CARD")
                .receiptUrl(generateReceiptUrl(paymentId))
                .build();
    }

    @Override
    protected void postProcessPayment(PaymentResult result) {
        // 카드 결제 후처리
        // 예: 결제 내역 저장, 알림 발송 등 (비동기 처리 권장)
        System.out.println("Post-processing card payment: " + result.getPaymentId());
    }


    // ==== 결제 취소 관련 메소드 ==== //
    @Override
    protected void validateCancellation(String paymentId, BigDecimal amount) {
        if (paymentId == null || paymentId.isBlank()) {
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
        // 카드 결제 취소 처리
        // 실제로는 카드사 시스템과 연동하여 취소 처리
        System.out.println("Processing cancellation for payment: " + paymentId +
                " with amount: " + amount);

        return new PaymentResult.Builder()
                .paymentId(paymentId)
                .status("CANCELLED")
                .approvedAmount(amount.negate())
                .approvedAt(LocalDateTime.now())
                .paymentMethodType("CARD")
                .receiptUrl(generateReceiptUrl(paymentId))
                .build();
    }

    @Override
    protected void postProcessCancellation(PaymentResult result) {
        // 카드 결제 취소 후처리
        // 예: 취소 내역 저장, 알림 발송 등
        System.out.println("Post-processing card payment cancellation: " + result.getPaymentId());
    }

    // 카드번호 마스킹 유틸리티 메소드
    private String maskCardNumber(String cardNumber) {
        if (cardNumber.length() < 8) {
            return cardNumber;
        }
        return cardNumber.substring(0, 4) + "-****-****-" +
                cardNumber.substring(cardNumber.length() - 4);
    }
}
