package io.hello.demo.paymentapi.domain.method;

import io.hello.demo.paymentapi.domain.request.v2.PaymentRequest;
import io.hello.demo.paymentapi.domain.PaymentResult;
import org.springframework.stereotype.Component;

@Component
public class VirtualAccountPaymentMethod implements PaymentMethod {

    @Override
    public boolean validate(PaymentRequest request) {
        return false;
    }

    @Override
    public PaymentResult execute(PaymentRequest request) {
        return null;
    }

    @Override
    public boolean supports(PaymentMethodType type) {
        return false;
    }
}
