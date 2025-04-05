package io.hello.demo.testmodule.paymentsystem.domain.processor;

import io.hello.demo.testmodule.paymentsystem.domain.AccountInfo;
import io.hello.demo.testmodule.paymentsystem.domain.PaymentRequest;
import io.hello.demo.testmodule.paymentsystem.domain.PaymentResult;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class AccountTransferPaymentProcessor extends AbstractPaymentProcessor {
    @Override
    protected void validatePaymentRequest(PaymentRequest request) {
        if (request.getAccountInfo() == null) {
            throw new IllegalArgumentException("Account information is required");
        }

        AccountInfo accountInfo = request.getAccountInfo();
        if (accountInfo.getBankCode() == null || accountInfo.getBankCode().isEmpty()) {
            throw new IllegalArgumentException("Bank code is required");
        }

        if (accountInfo.getAccountNumber() == null || accountInfo.getAccountNumber().isEmpty()) {
            throw new IllegalArgumentException("Account number is required");
        }
    }

    @Override
    protected void authenticatePayment(PaymentRequest request) {
        // 계좌이체 인증 절차
        // 실제로는 은행 시스템과 연동하여 인증 처리
        AccountInfo accountInfo = request.getAccountInfo();
        System.out.println("Authenticating account transfer with bank code: " +
                accountInfo.getBankCode() + " and account: " + maskAccountNumber(accountInfo.getAccountNumber()));
    }

    @Override
    protected PaymentResult authorizePayment(PaymentRequest request) {
        // 계좌이체 승인 절차
        // 실제로는 은행 시스템과 연동하여 승인 처리
        String paymentId = generatePaymentId();

        System.out.println("Authorizing account transfer for order: " + request.getOrderId() +
                " with amount: " + request.getAmount());

        return new PaymentResult.Builder()
                .paymentId(paymentId)
                .status("APPROVED")
                .approvedAmount(request.getAmount())
                .approvedAt(LocalDateTime.now())
                .paymentMethodType("ACCOUNT_TRANSFER")
                .receiptUrl(generateReceiptUrl(paymentId))
                .build();
    }

    @Override
    protected void postProcessPayment(PaymentResult result) {
        // 계좌이체 후처리
        // 예: 결제 내역 저장, 알림 발송 등
        System.out.println("Post-processing account transfer: " + result.getPaymentId());
    }

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
        // 계좌이체 취소 처리
        // 실제로는 은행 시스템과 연동하여 취소 처리
        System.out.println("Processing cancellation for payment: " + paymentId +
                " with amount: " + amount);

        return new PaymentResult.Builder()
                .paymentId(paymentId)
                .status("CANCELLED")
                .approvedAmount(amount.negate())
                .approvedAt(LocalDateTime.now())
                .paymentMethodType("ACCOUNT_TRANSFER")
                .receiptUrl(generateReceiptUrl(paymentId))
                .build();
    }

    @Override
    protected void postProcessCancellation(PaymentResult result) {
        // 계좌이체 취소 후처리
        // 예: 취소 내역 저장, 알림 발송 등
        System.out.println("Post-processing account transfer cancellation: " + result.getPaymentId());
    }

    private String maskAccountNumber(String accountNumber) {
        if (accountNumber.length() < 6) {
            return accountNumber;
        }
        return accountNumber.substring(0, 2) + "****" +
                accountNumber.substring(accountNumber.length() - 2);
    }
}
