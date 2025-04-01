package io.hello.demo.paymentapi.domain.method;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PaymentMethodFactory {

    private final List<PaymentMethod> paymentMethods;

    public PaymentMethodFactory(List<PaymentMethod> paymentMethods) {
        this.paymentMethods = paymentMethods;
    }

    public PaymentMethod getPaymentMethod(PaymentMethodType type) {
        return paymentMethods.stream()
                .filter(paymentMethod -> paymentMethod.supports(type))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unsupported payment method type: " + type.name()));
    }
}
