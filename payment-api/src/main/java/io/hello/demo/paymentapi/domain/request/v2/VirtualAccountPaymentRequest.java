package io.hello.demo.paymentapi.domain.request.v2;

public record VirtualAccountPaymentRequest(
        Long amount,
        String bankCode,
        String accountNumber,
        String holderName
) implements PaymentRequest {
}
