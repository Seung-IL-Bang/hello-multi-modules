package io.hello.demo.testmodule.domain.processor;

import io.hello.demo.testmodule.domain.PaymentRequest;
import io.hello.demo.testmodule.domain.PaymentResult;

import java.math.BigDecimal;

public interface PaymentProcessor {
    PaymentResult processPayment(PaymentRequest request);
    PaymentResult cancelPayment(String paymentId, BigDecimal amount);
}
