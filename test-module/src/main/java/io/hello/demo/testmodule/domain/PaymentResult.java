package io.hello.demo.testmodule.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class PaymentResult {

    private String paymentId;
    private String status;
    private BigDecimal approvedAmount;
    private LocalDateTime approvedAt;
    private String paymentMethodType;
    private String receiptUrl;

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

    public static class Builder {
        private String paymentId;
        private String status;
        private BigDecimal approvedAmount;
        private LocalDateTime approvedAt;
        private String paymentMethodType;
        private String receiptUrl;

        public Builder paymentId(String paymentId) {
            this.paymentId = paymentId;
            return this;
        }
        public Builder status(String status) {
            this.status = status;
            return this;
        }
        public Builder approvedAmount(BigDecimal approvedAmount) {
            this.approvedAmount = approvedAmount;
            return this;
        }
        public Builder approvedAt(LocalDateTime approvedAt) {
            this.approvedAt = approvedAt;
            return this;
        }
        public Builder paymentMethodType(String paymentMethodType) {
            this.paymentMethodType = paymentMethodType;
            return this;
        }
        public Builder receiptUrl(String receiptUrl) {
            this.receiptUrl = receiptUrl;
            return this;
        }
        public PaymentResult build() {
            PaymentResult paymentResult = new PaymentResult();
            paymentResult.setPaymentId(paymentId);
            paymentResult.setStatus(status);
            paymentResult.setApprovedAmount(approvedAmount);
            paymentResult.setApprovedAt(approvedAt);
            paymentResult.setPaymentMethodType(paymentMethodType);
            paymentResult.setReceiptUrl(receiptUrl);
            return paymentResult;
        }
    }

}
