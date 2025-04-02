package io.hello.demo.paymentapi.domain.method;

import io.hello.demo.paymentapi.domain.request.PaymentRequest;
import io.hello.demo.paymentapi.domain.PaymentResult;
import org.springframework.stereotype.Component;

@Component
public class VirtualAccountPaymentMethod implements PaymentMethod {

    @Override
    public void validate(PaymentRequest request) {
    }

    @Override
    public PaymentResult pay(PaymentRequest request, String transactionId) {
        return null;
    }

    @Override
    public boolean supports(PaymentMethodType type) {
        return false;
    }
}
