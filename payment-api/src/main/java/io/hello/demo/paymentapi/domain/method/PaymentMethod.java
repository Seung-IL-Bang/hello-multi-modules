package io.hello.demo.paymentapi.domain.method;

import io.hello.demo.paymentapi.domain.PaymentResult;
import io.hello.demo.paymentapi.domain.request.PaymentRequest;

public interface PaymentMethod {
    void validate(PaymentRequest request);
    PaymentResult pay(PaymentRequest request, String transactionId);
    boolean supports(PaymentMethodType type);
}
