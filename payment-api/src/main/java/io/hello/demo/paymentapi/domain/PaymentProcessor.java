package io.hello.demo.paymentapi.domain;

public interface PaymentProcessor {
    PaymentResult process(PaymentRequest request);
}
