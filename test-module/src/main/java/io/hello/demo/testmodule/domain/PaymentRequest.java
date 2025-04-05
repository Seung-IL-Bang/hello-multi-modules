package io.hello.demo.testmodule.domain;

import java.math.BigDecimal;

public class PaymentRequest {

    private String paymentMethodType;
    private BigDecimal amount;
    private String orderId;
    private CustomerInfo customerInfo;
    private CardInfo cardInfo;
    private AccountInfo accountInfo;
    private TossPayInfo tossPayInfo;


    public String getPaymentMethodType() {
        return paymentMethodType;
    }

    public void setPaymentMethodType(String paymentMethodType) {
        this.paymentMethodType = paymentMethodType;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public CustomerInfo getCustomerInfo() {
        return customerInfo;
    }

    public void setCustomerInfo(CustomerInfo customerInfo) {
        this.customerInfo = customerInfo;
    }

    public CardInfo getCardInfo() {
        return cardInfo;
    }

    public void setCardInfo(CardInfo cardInfo) {
        this.cardInfo = cardInfo;
    }

    public AccountInfo getAccountInfo() {
        return accountInfo;
    }

    public void setAccountInfo(AccountInfo accountInfo) {
        this.accountInfo = accountInfo;
    }

    public TossPayInfo getTossPayInfo() {
        return tossPayInfo;
    }

    public void setTossPayInfo(TossPayInfo tossPayInfo) {
        this.tossPayInfo = tossPayInfo;
    }
}
