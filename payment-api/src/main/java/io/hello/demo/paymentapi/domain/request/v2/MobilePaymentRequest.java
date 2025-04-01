package io.hello.demo.paymentapi.domain.request.v2;

public record MobilePaymentRequest(
        Long amount,
        String phoneNumber,
        String carrierCode,
        String authCode
) implements PaymentRequest{
}
