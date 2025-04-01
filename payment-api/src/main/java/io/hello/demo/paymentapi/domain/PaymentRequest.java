package io.hello.demo.paymentapi.domain;

public record PaymentRequest(
        Long amount,
        String cardNumber,
        String cardExpiry,
        String cardCvc,
        String merchantId) {
}
