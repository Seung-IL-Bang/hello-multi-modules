package io.hello.demo.testmodule.unittest.paymentsystem;

import java.math.BigDecimal;

public class PaymentRequest {
    private BigDecimal amount;
    private String customerId;
    private CardInfo cardInfo;

    // Getters and Setters

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }

    public String getCustomerId() { return customerId; }
    public void setCustomerId(String customerId) { this.customerId = customerId; }

    public CardInfo getCardInfo() { return cardInfo; }
    public void setCardInfo(CardInfo cardInfo) { this.cardInfo = cardInfo; }

    public static class CardInfo {
        private String cardNumber;
        private String expiryDate;
        private String cvv;

        // Getters and Setters

        public String getCardNumber() { return cardNumber; }
        public void setCardNumber(String cardNumber) { this.cardNumber = cardNumber; }

        public String getExpiryDate() { return expiryDate; }
        public void setExpiryDate(String expiryDate) { this.expiryDate = expiryDate; }

        public String getCvv() { return cvv; }
        public void setCvv(String cvv) { this.cvv = cvv; }
    }
}
