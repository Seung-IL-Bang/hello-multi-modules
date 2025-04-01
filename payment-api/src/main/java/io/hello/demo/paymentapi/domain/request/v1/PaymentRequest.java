package io.hello.demo.paymentapi.domain.request.v1;

public record PaymentRequest(
        Long amount,
        String cardNumber,
        String cardExpiry,
        String cardCvc,
        String merchantId) {
}
