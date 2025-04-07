package io.hello.demo.webmodule.support.error;

import org.springframework.boot.logging.LogLevel;
import org.springframework.http.HttpStatus;

public enum ErrorType {

    DEFAULT_ERROR(
            HttpStatus.INTERNAL_SERVER_ERROR,
            ErrorCode.E500,
            "Internal Server Error",
            LogLevel.ERROR
    ),
    UNKNOWN_ERROR(
            HttpStatus.INTERNAL_SERVER_ERROR,
            ErrorCode.E500,
            "Unknown Error",
            LogLevel.ERROR
    ),
    NETWORK_ERROR(
            HttpStatus.INTERNAL_SERVER_ERROR,
            ErrorCode.E500,
            "Network Error",
            LogLevel.ERROR
    ),
    READ_TIMEOUT(
            HttpStatus.INTERNAL_SERVER_ERROR,
            ErrorCode.E500,
            "Read Timeout",
            LogLevel.ERROR
    ),
    CONNECTION_TIMEOUT(
            HttpStatus.INTERNAL_SERVER_ERROR,
            ErrorCode.E500,
            "Connection Timeout",
            LogLevel.ERROR);


    private final HttpStatus status;
    private final ErrorCode code;
    private final String message;
    private final LogLevel logLevel;

    ErrorType(
            HttpStatus status,
            ErrorCode code,
            String message,
            LogLevel logLevel
    ) {
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
