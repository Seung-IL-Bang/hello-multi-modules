package io.hello.demo.testmodule.unittest.paymentsystem;

public class InvalidPaymentException extends RuntimeException {
    public InvalidPaymentException(String message) {
        super(message);
    }
}
