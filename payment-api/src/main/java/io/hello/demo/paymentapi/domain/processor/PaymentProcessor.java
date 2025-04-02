package io.hello.demo.paymentapi.domain.processor;

import io.hello.demo.paymentapi.domain.PaymentContext;
import io.hello.demo.paymentapi.domain.PaymentResult;

public interface PaymentProcessor {
    PaymentResult process(PaymentContext context, String transactionId);
}
