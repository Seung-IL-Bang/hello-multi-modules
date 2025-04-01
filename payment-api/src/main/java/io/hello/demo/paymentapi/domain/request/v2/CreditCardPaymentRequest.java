package io.hello.demo.paymentapi.domain.request.v2;

public record CreditCardPaymentRequest(
        Long amount,
        String cardNumber,
        String cardExpiry,
        String cardCvc,
        String merchantId
) implements PaymentRequest{
}
