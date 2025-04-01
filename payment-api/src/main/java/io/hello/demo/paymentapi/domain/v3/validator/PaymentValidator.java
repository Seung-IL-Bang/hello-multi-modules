package io.hello.demo.paymentapi.domain.v3.validator;

import io.hello.demo.paymentapi.domain.PaymentRequest;

public interface PaymentValidator {
    void validate(PaymentRequest request);
}
