package io.hello.demo.asyncmodule.support.error;

import org.springframework.boot.logging.LogLevel;
import org.springframework.http.HttpStatus;

public enum ErrorType {

    DEFAULT(HttpStatus.INTERNAL_SERVER_ERROR,
            ErrorCode.E500,
            "Internal Server Error",
            LogLevel.ERROR),
    FAILED_NOFITY_PAYMENT_RESULT(
            HttpStatus.INTERNAL_SERVER_ERROR,
            ErrorCode.E500,
            "[알림 서비스] 알림 발송 실패",
            LogLevel.ERROR)
    ;

    private final HttpStatus status;
    private final ErrorCode code;
    private final String message;
    private final LogLevel logLevel;

    ErrorType(HttpStatus status, ErrorCode code, String message, LogLevel logLevel) {
        this.status = status;
        this.code = code;
        this.message = message;
        this.logLevel = logLevel;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public ErrorCode getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public LogLevel getLogLevel() {
        return logLevel;
    }
}
