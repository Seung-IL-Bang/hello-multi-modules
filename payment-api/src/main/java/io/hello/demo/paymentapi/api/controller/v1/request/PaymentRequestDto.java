package io.hello.demo.paymentapi.api.controller.v1.request;

import io.hello.demo.paymentapi.domain.request.v1.PaymentRequest;

public record PaymentRequestDto(
        Long amount,
        String cardNumber,
        String cardExpiry,
        String cardCvc,
        String merchantId) {
    public PaymentRequest toPaymentData() {
        return new PaymentRequest(
                amount,
                cardNumber,
                cardExpiry,
                cardCvc,
                merchantId
        );
    }
}
