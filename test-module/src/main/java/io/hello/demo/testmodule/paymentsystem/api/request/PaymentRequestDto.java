package io.hello.demo.testmodule.paymentsystem.api.request;

import io.hello.demo.testmodule.domain.*;
import io.hello.demo.testmodule.paymentsystem.domain.*;

import java.math.BigDecimal;

public class PaymentRequestDto {

    private String paymentMethodType;
    private BigDecimal amount;
    private String orderId;
    private CustomerInfo customerInfo;
    private CardInfo cardInfo;
    private AccountInfo accountInfo;
    private TossPayInfo tossPayInfo;

    public PaymentRequest toPaymentRequest() {

        // 공통 요청 필드 기본 검증
        if (this.getAmount() == null || this.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Invalid payment amount");
        }

        if (this.getPaymentMethodType() == null || this.getPaymentMethodType().isBlank()) {
            throw new IllegalArgumentException("Payment method type is required");
        }

        if (this.getOrderId() == null || this.getOrderId().isBlank()) {
            throw new IllegalArgumentException("Order ID is required");
        }

        if (this.getCustomerInfo() == null) {
            throw new IllegalArgumentException("Customer information is required");
        }

        if (this.getCardInfo() == null
            && this.getAccountInfo() == null
            && this.getTossPayInfo() == null) {
            throw new IllegalArgumentException("At least one payment method information is required");
        }

        // 결제 요청 객체 생성
        PaymentRequest request = new PaymentRequest();
        request.setPaymentMethodType(this.paymentMethodType);
        request.setAmount(this.amount);
        request.setOrderId(this.orderId);
        request.setCustomerInfo(this.customerInfo);
        request.setCardInfo(this.cardInfo);
        request.setAccountInfo(this.accountInfo);
        request.setTossPayInfo(this.tossPayInfo);
        return request;
    }

    public String getPaymentMethodType() {
        return paymentMethodType;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public String getOrderId() {
        return orderId;
    }

    public CustomerInfo getCustomerInfo() {
        return customerInfo;
    }

    public CardInfo getCardInfo() {
        return cardInfo;
    }

    public AccountInfo getAccountInfo() {
        return accountInfo;
    }

    public TossPayInfo getTossPayInfo() {
        return tossPayInfo;
    }
}
