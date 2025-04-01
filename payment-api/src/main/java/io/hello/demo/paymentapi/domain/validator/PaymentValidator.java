package io.hello.demo.paymentapi.domain.validator;

import io.hello.demo.paymentapi.domain.PaymentRequest;

public interface PaymentValidator {
    void validate(PaymentRequest request);
}
