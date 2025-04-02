package io.hello.demo.sseapi;

public record PaymentResultEvent(
        String userId,
        String transactionId,
        boolean success,
        String message,
        String timeStamp
) {
}
