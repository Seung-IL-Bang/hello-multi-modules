package io.hello.demo.paymentapi.domain.validator.v1;

import io.hello.demo.paymentapi.domain.request.v1.PaymentRequest;

public interface PaymentValidator {
    void validate(PaymentRequest request);
}
