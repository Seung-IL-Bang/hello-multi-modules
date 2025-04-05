package io.hello.demo.testmodule.paymentsystem.domain.processor;

public class PaymentProcessorFactory {

    public static PaymentProcessor createPaymentProcessor(String paymentMethodType) {
        return switch (paymentMethodType) {
            case "CARD" -> new CardPaymentProcessor();
            case "ACCOUNT_TRANSFER" -> new AccountTransferPaymentProcessor();
            case "TOSS_PAY" -> new TossPayPaymentProcessor();
            default -> throw new IllegalArgumentException("Unsupported payment method type: " + paymentMethodType);
        };
    }
}
