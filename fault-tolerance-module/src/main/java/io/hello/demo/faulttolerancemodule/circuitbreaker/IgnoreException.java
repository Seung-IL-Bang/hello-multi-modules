package io.hello.demo.faulttolerancemodule.circuitbreaker;

public class IgnoreException extends RuntimeException {
    public IgnoreException(String message) {
        super(message);
    }
}
