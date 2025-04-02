package io.hello.demo.paymentapi.domain.processor;

import io.hello.demo.paymentapi.domain.PaymentContext;
import io.hello.demo.paymentapi.domain.PaymentResult;

public interface PaymentProcessorV2 {
    PaymentResult process(PaymentContext context, String transactionId);
}
