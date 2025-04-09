package io.hello.demo.faulttolerancemodule;

public class IgnoreException extends RuntimeException {
    public IgnoreException(String message) {
        super(message);
    }
}
