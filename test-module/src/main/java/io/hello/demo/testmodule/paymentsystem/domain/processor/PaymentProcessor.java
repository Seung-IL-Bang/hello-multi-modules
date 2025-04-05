package io.hello.demo.testmodule.paymentsystem.domain.processor;

import io.hello.demo.testmodule.paymentsystem.domain.PaymentRequest;
import io.hello.demo.testmodule.paymentsystem.domain.PaymentResult;

import java.math.BigDecimal;

public interface PaymentProcessor {
    PaymentResult processPayment(PaymentRequest request);
    PaymentResult cancelPayment(String paymentId, BigDecimal amount);
}
