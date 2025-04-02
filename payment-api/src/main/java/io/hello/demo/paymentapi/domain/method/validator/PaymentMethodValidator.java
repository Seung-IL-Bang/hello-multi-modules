package io.hello.demo.paymentapi.domain.method.validator;

import io.hello.demo.paymentapi.domain.request.PaymentRequest;

public interface PaymentMethodValidator {
    void validate(PaymentRequest request);

    boolean supports(PaymentRequest request);
}
