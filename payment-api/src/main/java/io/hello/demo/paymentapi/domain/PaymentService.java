package io.hello.demo.paymentapi.domain;

public interface PaymentService {
    PaymentResult processPayment(PaymentRequest request, String productId, int quantity);
}
