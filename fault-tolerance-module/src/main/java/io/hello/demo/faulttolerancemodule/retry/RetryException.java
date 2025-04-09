package io.hello.demo.faulttolerancemodule.retry;

public class RetryException extends RuntimeException {
    public RetryException(String message) {
        super(message);
    }
}
