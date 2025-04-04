package io.hello.demo.asyncmodule.support.error;

public class AsyncException extends RuntimeException {

    private final ErrorType errorType;

    public AsyncException(ErrorType errorType) {
        super(errorType.getMessage());
        this.errorType = errorType;
    }

    public ErrorType getErrorType() {
        return errorType;
    }
}
