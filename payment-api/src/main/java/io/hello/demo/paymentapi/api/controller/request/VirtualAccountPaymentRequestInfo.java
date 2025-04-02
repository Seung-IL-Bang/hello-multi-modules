package io.hello.demo.paymentapi.api.controller.request;

import io.hello.demo.paymentapi.domain.request.v2.PaymentRequest;
import io.hello.demo.paymentapi.domain.request.v2.VirtualAccountPaymentRequest;

public record VirtualAccountPaymentRequestInfo(
        Long amount,
        String bankCode,
        String accountNumber,
        String holderName
) {
    public PaymentRequest toPaymentRequest() {
        return new VirtualAccountPaymentRequest(
                this.amount(),
                this.bankCode(),
                this.accountNumber(),
                this.holderName()
        );
    }
}
