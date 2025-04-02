package io.hello.demo.paymentapi.domain.request;

public record VirtualAccountPaymentRequest(
        Long amount,
        String bankCode,
        String accountNumber,
        String holderName
) implements PaymentRequest {
}
