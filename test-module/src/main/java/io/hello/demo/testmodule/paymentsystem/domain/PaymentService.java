package io.hello.demo.testmodule.paymentsystem.domain;

import io.hello.demo.testmodule.paymentsystem.domain.processor.LoggingPaymentDecorator;
import io.hello.demo.testmodule.paymentsystem.domain.processor.PaymentProcessor;
import io.hello.demo.testmodule.paymentsystem.domain.processor.PaymentProcessorFactory;
import io.hello.demo.testmodule.paymentsystem.domain.processor.SecurityPaymentDecorator;

import java.math.BigDecimal;

public class PaymentService {

    public PaymentResult processPayment(PaymentRequest request) {
        // 요청된 결제 수단에 따라 적절한 처리기 생성
        PaymentProcessor baseProcessor = PaymentProcessorFactory.createPaymentProcessor(
                request.getPaymentMethodType());

        // 데코레이터를 적용하여 부가 기능 추가
        PaymentProcessor processor = new SecurityPaymentDecorator(
                new LoggingPaymentDecorator(baseProcessor));

        // 결제 처리
        return processor.processPayment(request);
    }

    public PaymentResult cancelPayment(String paymentId, String paymentMethodType, BigDecimal amount) {
        // 취소할 결제 수단에 따라 적절한 처리기 생성
        PaymentProcessor baseProcessor = PaymentProcessorFactory.createPaymentProcessor(paymentMethodType);

        // 데코레이터를 적용하여 부가 기능 추가
        PaymentProcessor processor = new SecurityPaymentDecorator(
                new LoggingPaymentDecorator(baseProcessor));

        // 결제 취소 처리
        return processor.cancelPayment(paymentId, amount);
    }
}
