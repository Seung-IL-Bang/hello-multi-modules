package io.hello.demo.testmodule.unittest;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class PaymentResponse {

    private String paymentId;
    private BigDecimal amount;
    private PaymentStatus status;
    private LocalDateTime approvedAt;
    private String message;

    // Getters and Setters

    public String getPaymentId() { return paymentId; }
    public void setPaymentId(String paymentId) { this.paymentId = paymentId; }

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }

    public PaymentStatus getStatus() { return status; }
    public void setStatus(PaymentStatus status) { this.status = status; }

    public LocalDateTime getApprovedAt() { return approvedAt; }
    public void setApprovedAt(LocalDateTime approvedAt) { this.approvedAt = approvedAt; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public static PaymentResponse success(Payment payment) {
        PaymentResponse response = new PaymentResponse();
        response.setPaymentId(payment.getId());
        response.setAmount(payment.getAmount());
        response.setStatus(payment.getStatus());
        response.setApprovedAt(payment.getUpdatedAt());
        response.setMessage("Payment processed successfully");
        return response;
    }
}
