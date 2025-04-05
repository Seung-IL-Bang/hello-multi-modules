package io.hello.demo.testmodule.paymentsystem.api.response;

import io.hello.demo.testmodule.paymentsystem.domain.PaymentResult;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class PaymentResultDto {

    private String paymentId;
    private String status;
    private BigDecimal approvedAmount;
    private LocalDateTime approvedAt;
    private String paymentMethodType;
    private String receiptUrl;

    public static PaymentResultDto of(PaymentResult paymentResult) {
        PaymentResultDto dto = new PaymentResultDto();
        dto.setPaymentId(paymentResult.getPaymentId());
        dto.setStatus(paymentResult.getStatus());
        dto.setApprovedAmount(paymentResult.getApprovedAmount());
        dto.setApprovedAt(paymentResult.getApprovedAt());
        dto.setPaymentMethodType(paymentResult.getPaymentMethodType());
        dto.setReceiptUrl(paymentResult.getReceiptUrl());
        return dto;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public BigDecimal getApprovedAmount() {
        return approvedAmount;
    }

    public void setApprovedAmount(BigDecimal approvedAmount) {
        this.approvedAmount = approvedAmount;
    }

    public LocalDateTime getApprovedAt() {
        return approvedAt;
    }

    public void setApprovedAt(LocalDateTime approvedAt) {
        this.approvedAt = approvedAt;
    }

    public String getPaymentMethodType() {
        return paymentMethodType;
    }

    public void setPaymentMethodType(String paymentMethodType) {
        this.paymentMethodType = paymentMethodType;
    }

    public String getReceiptUrl() {
        return receiptUrl;
    }

    public void setReceiptUrl(String receiptUrl) {
        this.receiptUrl = receiptUrl;
    }
}
