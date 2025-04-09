package io.hello.demo.faulttolerancemodule;

public class RecordException extends RuntimeException {
    public RecordException(String message) {
        super(message);
    }
}
