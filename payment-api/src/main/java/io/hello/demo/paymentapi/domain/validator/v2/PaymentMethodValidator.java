package io.hello.demo.paymentapi.domain.validator.v2;

import io.hello.demo.paymentapi.domain.request.v2.PaymentRequest;

public interface PaymentMethodValidator {
    void validate(PaymentRequest request);
}
