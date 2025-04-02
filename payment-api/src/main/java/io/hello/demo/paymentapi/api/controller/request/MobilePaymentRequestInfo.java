package io.hello.demo.paymentapi.api.controller.request;

import io.hello.demo.paymentapi.domain.request.MobilePaymentRequest;
import io.hello.demo.paymentapi.domain.request.PaymentRequest;

public record MobilePaymentRequestInfo(
        Long amount,
        String phoneNumber,
        String carrierCode,
        String authCode
) {
    public PaymentRequest toPaymentRequest() {
        return new MobilePaymentRequest(
                this.amount(),
                this.phoneNumber(),
                this.carrierCode(),
                this.authCode()
        );
    }
}
