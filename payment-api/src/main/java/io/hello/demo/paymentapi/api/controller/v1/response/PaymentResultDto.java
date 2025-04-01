package io.hello.demo.paymentapi.api.controller.v1.response;

import io.hello.demo.paymentapi.domain.PaymentResult;
import io.hello.demo.paymentapi.domain.PaymentStatus;

import java.time.LocalDateTime;

public class PaymentResultDto {

    private final String transactionId;
    private final PaymentStatus status;
    private final LocalDateTime approvedAt;
    private final String errorCode;
    private final String errorMessage;

    public PaymentResultDto(PaymentResult paymentResult) {
        this.transactionId = paymentResult.transactionId();
        this.status = paymentResult.status();
        this.approvedAt = paymentResult.approvedAt();
        this.errorCode = paymentResult.errorCode();
        this.errorMessage = paymentResult.errorMessage();
    }

    public String getTransactionId() {
        return transactionId;
    }

    public PaymentStatus getStatus() {
        return status;
    }

    public LocalDateTime getApprovedAt() {
        return approvedAt;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
