package io.hello.demo.paymentapi.api.controller.v2.request;

public record CreditCardPaymentRequestDto(
        Long amount,
        String cardNumber,
        String cardExpiry,
        String cardCvc,
        String merchantId
) {
}
