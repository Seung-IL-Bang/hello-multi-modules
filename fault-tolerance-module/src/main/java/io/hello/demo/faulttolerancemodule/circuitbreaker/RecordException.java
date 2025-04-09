package io.hello.demo.faulttolerancemodule.circuitbreaker;

public class RecordException extends RuntimeException {
    public RecordException(String message) {
        super(message);
    }
}
