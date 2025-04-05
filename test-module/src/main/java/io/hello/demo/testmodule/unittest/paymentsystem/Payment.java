package io.hello.demo.testmodule.unittest.paymentsystem;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Payment {

    private String id;
    private BigDecimal amount;
    private PaymentStatus status;
    private String customerId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String pgTraceId;
    private String pgCancelId;
    private BigDecimal cancelledAmount = BigDecimal.ZERO;
    private String failReason;

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }

    public PaymentStatus getStatus() { return status; }
    public void setStatus(PaymentStatus status) { this.status = status; }

    public String getCustomerId() { return customerId; }
    public void setCustomerId(String customerId) { this.customerId = customerId; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public String getPgTraceId() { return pgTraceId; }
    public void setPgTraceId(String pgTraceId) { this.pgTraceId = pgTraceId; }

    public String getPgCancelId() { return pgCancelId; }
    public void setPgCancelId(String pgCancelId) { this.pgCancelId = pgCancelId; }

    public BigDecimal getCancelledAmount() { return cancelledAmount; }
    public void setCancelledAmount(BigDecimal cancelledAmount) { this.cancelledAmount = cancelledAmount; }

    public String getFailReason() { return failReason; }
    public void setFailReason(String failReason) { this.failReason = failReason; }

    // 비즈니스 메소드

    public void approve(String pgTraceId) {
        this.pgTraceId = pgTraceId;
        this.status = PaymentStatus.APPROVED;
        this.updatedAt = LocalDateTime.now();
    }

    public void fail(String reason) {
        this.status = PaymentStatus.FAILED;
        this.failReason = reason;
        this.updatedAt = LocalDateTime.now();
    }

    public void cancel(String pgCancelId, BigDecimal amount) {
        this.pgCancelId = pgCancelId;
        this.cancelledAmount = amount;

        if (this.amount.equals(amount)) {
            this.status = PaymentStatus.CANCELLED;
        } else {
            this.status = PaymentStatus.PARTIALLY_CANCELLED;
        }

        this.updatedAt = LocalDateTime.now();
    }

    public boolean canCancel() {
        return this.status == PaymentStatus.APPROVED ||
                this.status == PaymentStatus.PARTIALLY_CANCELLED;
    }

    public boolean isFullyCancelled() {
        return this.status == PaymentStatus.CANCELLED;
    }

    public BigDecimal getRefundableAmount() {
        return this.amount.subtract(this.cancelledAmount);
    }
}
