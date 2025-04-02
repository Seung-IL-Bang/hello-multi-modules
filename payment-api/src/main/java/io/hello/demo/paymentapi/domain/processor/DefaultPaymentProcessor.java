package io.hello.demo.paymentapi.domain.processor;

import io.hello.demo.paymentapi.domain.PaymentContext;
import io.hello.demo.paymentapi.domain.PaymentResult;
import io.hello.demo.paymentapi.domain.method.PaymentMethod;
import io.hello.demo.paymentapi.domain.method.PaymentMethodFactory;
import org.springframework.stereotype.Service;

@Service
public class DefaultPaymentProcessor implements PaymentProcessor {

    private final PaymentMethodFactory paymentMethodFactory;

    public DefaultPaymentProcessor(PaymentMethodFactory paymentMethodFactory) {
        this.paymentMethodFactory = paymentMethodFactory;
    }

    @Override
    public PaymentResult process(PaymentContext paymentContext, String transactionId) {
        PaymentMethod paymentMethod = paymentMethodFactory.getPaymentMethod(paymentContext.paymentMethodType());
        return paymentMethod.pay(paymentContext.paymentRequest(), transactionId);
    }
}
