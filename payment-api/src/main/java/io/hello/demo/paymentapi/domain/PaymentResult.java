package io.hello.demo.paymentapi.domain;

import java.time.LocalDateTime;

public record PaymentResult(
        String transactionId,
        PaymentStatus status,
        LocalDateTime approvedAt,
        String errorCode,
        String errorMessage) {

    // Builder 패턴 사용
    public static class Builder {
        private String transactionId;
        private PaymentStatus status;
        private LocalDateTime approvedAt;
        private String errorCode;
        private String errorMessage;

        public Builder transactionId(String transactionId) {
            this.transactionId = transactionId;
            return this;
        }

        public Builder status(PaymentStatus status) {
            this.status = status;
            return this;
        }

        public Builder approvedAt(LocalDateTime approvedAt) {
            this.approvedAt = approvedAt;
            return this;
        }

        public Builder errorCode(String errorCode) {
            this.errorCode = errorCode;
            return this;
        }

        public Builder errorMessage(String errorMessage) {
            this.errorMessage = errorMessage;
            return this;
        }

        public PaymentResult build() {
            return new PaymentResult(transactionId, status, approvedAt, errorCode, errorMessage);
        }
    }
}
