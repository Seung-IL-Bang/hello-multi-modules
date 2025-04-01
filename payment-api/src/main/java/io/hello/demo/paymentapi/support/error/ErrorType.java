package io.hello.demo.paymentapi.support.error;

import org.springframework.boot.logging.LogLevel;
import org.springframework.http.HttpStatus;

public enum ErrorType {
    DEFAULT_ERROR(
            HttpStatus.INTERNAL_SERVER_ERROR,
            ErrorCode.E500,
            "An unexpected error has occurred.",
            LogLevel.ERROR
    ),
    INVALID_PAYMENT_AMOUNT(
            HttpStatus.BAD_REQUEST,
            ErrorCode.E400,
            "결제 금액이 0보다 작습니다.",
            LogLevel.WARN
    ),
    INVALID_PAYMENT_CARD_NUMBER(
            HttpStatus.BAD_REQUEST,
            ErrorCode.E400,
            "카드번호는 16자리 숫자여야 합니다.",
            LogLevel.WARN
    ),
    INVALID_PAYMENT_CARD_EXPIRY(
            HttpStatus.BAD_REQUEST,
            ErrorCode.E400,
            "유효기간 형식은 MM/YY 이어야 합니다.",
            LogLevel.WARN),
    INVALID_PAYMENT_CARD_CVC(
            HttpStatus.BAD_REQUEST,
            ErrorCode.E400,
            "CVC는 3자리 숫자여야 합니다.",
            LogLevel.WARN
    );

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
