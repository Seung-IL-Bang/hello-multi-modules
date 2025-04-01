package io.hello.demo.paymentapi.domain.method;

import io.hello.demo.paymentapi.domain.PaymentResult;
import io.hello.demo.paymentapi.domain.request.v2.PaymentRequest;

public interface PaymentMethod {
    boolean validate(PaymentRequest request);
    PaymentResult execute(PaymentRequest request);
    boolean supports(PaymentMethodType type);
}
