package io.hello.demo.paymentapi.domain.v2;

import io.hello.demo.paymentapi.domain.PaymentRequest;

public interface PaymentValidator {
    void validate(PaymentRequest request);
}
