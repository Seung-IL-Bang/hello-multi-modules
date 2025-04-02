package io.hello.demo.paymentapi.domain;

import io.hello.demo.paymentapi.domain.method.PaymentMethodType;
import io.hello.demo.paymentapi.domain.request.PaymentRequest;

public record PaymentContext(
        PaymentMethodType paymentMethodType,
        PaymentRequest paymentRequest
) {
}
