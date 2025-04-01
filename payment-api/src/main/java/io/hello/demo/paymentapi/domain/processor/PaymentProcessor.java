package io.hello.demo.paymentapi.domain.processor;

import io.hello.demo.paymentapi.domain.request.v1.PaymentRequest;
import io.hello.demo.paymentapi.domain.PaymentResult;

public interface PaymentProcessor {
    PaymentResult process(PaymentRequest request);
}
