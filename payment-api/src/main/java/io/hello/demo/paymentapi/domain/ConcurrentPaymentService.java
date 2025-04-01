package io.hello.demo.paymentapi.domain;

public interface ConcurrentPaymentService {
    PaymentResult processPayment(PaymentRequest request, String productId, int quantity);
}
