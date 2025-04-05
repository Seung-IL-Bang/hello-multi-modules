package io.hello.demo.testmodule.unittest.paymentsystem;

public class PaymentNotFoundException extends RuntimeException {
    public PaymentNotFoundException(String message) {
        super(message);
    }
}
