package io.hello.demo.testmodule.domain.processor;

import io.hello.demo.testmodule.domain.PaymentRequest;
import io.hello.demo.testmodule.domain.PaymentResult;

import java.math.BigDecimal;

public class LoggingPaymentDecorator extends PaymentProcessorDecorator {

    public LoggingPaymentDecorator(PaymentProcessor processor) {
        super(processor);
    }

    @Override
    public PaymentResult processPayment(PaymentRequest request) {
        System.out.println("LOG: Payment processing started for order " + request.getOrderId());
        long startTime = System.currentTimeMillis();

        try {
            PaymentResult result = processor.processPayment(request);
            System.out.println("LOG: Payment processing completed for order " + request.getOrderId() +
                    " with status: " + result.getStatus() +
                    " in " + (System.currentTimeMillis() - startTime) + "ms");
            return result;
        } catch (Exception e) {
            System.out.println("LOG: Payment processing failed for order " + request.getOrderId() +
                    " with error: " + e.getMessage() +
                    " in " + (System.currentTimeMillis() - startTime) + "ms");
            throw e;
        }
    }

    @Override
    public PaymentResult cancelPayment(String paymentId, BigDecimal amount) {
        System.out.println("LOG: Payment cancellation started for payment " + paymentId);
        long startTime = System.currentTimeMillis();

        try {
            PaymentResult result = processor.cancelPayment(paymentId, amount);
            System.out.println("LOG: Payment cancellation completed for payment " + paymentId +
                    " with status: " + result.getStatus() +
                    " in " + (System.currentTimeMillis() - startTime) + "ms");
            return result;
        } catch (Exception e) {
            System.out.println("LOG: Payment cancellation failed for payment " + paymentId +
                    " with error: " + e.getMessage() +
                    " in " + (System.currentTimeMillis() - startTime) + "ms");
            throw e;
        }
    }
}
