package io.hello.demo.paymentapi.domain.processor.v4;

import io.hello.demo.paymentapi.domain.PaymentContext;
import io.hello.demo.paymentapi.domain.PaymentResult;
import io.hello.demo.paymentapi.domain.method.PaymentMethod;
import io.hello.demo.paymentapi.domain.method.PaymentMethodFactory;
import io.hello.demo.paymentapi.domain.processor.PaymentProcessorV2;
import org.springframework.stereotype.Service;

@Service
public class DefaultPaymentProcessorV4 implements PaymentProcessorV2 {

    private final PaymentMethodFactory paymentMethodFactory;

    public DefaultPaymentProcessorV4(PaymentMethodFactory paymentMethodFactory) {
        this.paymentMethodFactory = paymentMethodFactory;
    }

    @Override
    public PaymentResult process(PaymentContext paymentContext) {
        PaymentMethod paymentMethod = paymentMethodFactory.getPaymentMethod(paymentContext.paymentMethodType());
        return paymentMethod.pay(paymentContext.paymentRequest());
    }
}
