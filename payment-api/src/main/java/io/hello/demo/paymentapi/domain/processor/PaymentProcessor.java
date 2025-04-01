package io.hello.demo.paymentapi.domain.processor;

import io.hello.demo.paymentapi.domain.PaymentRequest;
import io.hello.demo.paymentapi.domain.PaymentResult;

public interface PaymentProcessor {
    PaymentResult process(PaymentRequest request);
}
