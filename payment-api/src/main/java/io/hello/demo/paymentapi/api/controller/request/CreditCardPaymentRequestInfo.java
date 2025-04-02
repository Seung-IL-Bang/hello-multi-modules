package io.hello.demo.paymentapi.api.controller.request;

import io.hello.demo.paymentapi.domain.request.CreditCardPaymentRequest;
import io.hello.demo.paymentapi.domain.request.PaymentRequest;

public record CreditCardPaymentRequestInfo(
        Long amount,
        String cardNumber,
        String cardExpiry,
        String cardCvc,
        String merchantId
) {
    public PaymentRequest toPaymentRequest() {
        return new CreditCardPaymentRequest(
                this.amount(),
                this.cardNumber(),
                this.cardExpiry(),
                this.cardCvc(),
                this.merchantId()
        );
    }
}
