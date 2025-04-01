package io.hello.demo.paymentapi.api.controller.v2.request;

public record VirtualAccountPaymentRequestDto(
        Long amount,
        String bankCode,
        String accountNumber,
        String holderName
) {
}
