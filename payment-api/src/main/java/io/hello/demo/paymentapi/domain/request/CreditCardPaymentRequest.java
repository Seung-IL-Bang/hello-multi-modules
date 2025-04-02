package io.hello.demo.paymentapi.domain.request;

public record CreditCardPaymentRequest(
        Long amount,
        String cardNumber,
        String cardExpiry,
        String cardCvc,
        String merchantId
) implements PaymentRequest {
}
