package io.hello.demo.testmodule.domain.processor;

import io.hello.demo.testmodule.domain.PaymentRequest;
import io.hello.demo.testmodule.domain.PaymentResult;

import java.math.BigDecimal;

// 기본 데코레이터 클래스 - 부가 기능을 위한 데코레이터 패턴 적용
public class PaymentProcessorDecorator implements PaymentProcessor {

    protected final PaymentProcessor processor;

    public PaymentProcessorDecorator(PaymentProcessor processor) {
        this.processor = processor;
    }

    @Override
    public PaymentResult processPayment(PaymentRequest request) {
        return processor.processPayment(request);
    }

    @Override
    public PaymentResult cancelPayment(String paymentId, BigDecimal amount) {
        return processor.cancelPayment(paymentId, amount);
    }
}
