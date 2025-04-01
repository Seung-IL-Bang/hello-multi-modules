package io.hello.demo.paymentapi.api.controller.v2.request;

public record MobilePaymentRequestDto(
        Long amount,
        String phoneNumber,
        String carrierCode,
        String authCode
) {
}
