package io.hello.demo.paymentapi.domain;

public interface PaymentService {
    PaymentResult processPayment(PaymentContext paymentContext, String productId, int quantity);
}
