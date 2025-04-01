package io.hello.demo.paymentapi.domain.processor.v1;

import io.hello.demo.paymentapi.domain.processor.PaymentProcessor;
import io.hello.demo.paymentapi.domain.request.v1.PaymentRequest;
import io.hello.demo.paymentapi.domain.PaymentResult;
import io.hello.demo.paymentapi.domain.PaymentStatus;
import io.hello.demo.paymentapi.support.error.CoreException;
import io.hello.demo.paymentapi.support.error.ErrorType;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.regex.Pattern;

@Service
public class DefaultPaymentProcessorV1 implements PaymentProcessor {

    private static final Pattern CARD_NUMBER_PATTERN = Pattern.compile("^\\d{4}-\\d{4}-\\d{4}-\\d{4}$");
    private static final Pattern CARD_EXPIRY_PATTERN = Pattern.compile("^(0[1-9]|1[0-2])/\\d{2}$");
    private static final Pattern CARD_CVC_PATTERN = Pattern.compile("^\\d{3}$");

    @Override
    public PaymentResult process(PaymentRequest request) {

        if (request.amount() < 0) {
            throw new CoreException(ErrorType.INVALID_PAYMENT_AMOUNT);
        }

        if (!CARD_NUMBER_PATTERN.matcher(request.cardNumber()).matches()) {
            throw new CoreException(ErrorType.INVALID_PAYMENT_CARD_NUMBER);
        }

        if (!CARD_EXPIRY_PATTERN.matcher(request.cardExpiry()).matches()) {
            throw new CoreException(ErrorType.INVALID_PAYMENT_CARD_EXPIRY);
        }

        if (!CARD_CVC_PATTERN.matcher(request.cardCvc()).matches()) {
            throw new CoreException(ErrorType.INVALID_PAYMENT_CARD_CVC);
        }

        // 실제 결제 로직 구현 (여기서는 항상 결제 성공하는 것으로 가정)
//        if (Math.random() < 0.2) {
//            return new PaymentResult.Builder()
//                    .transactionId(generateUniqueTransactionId())
//                    .status(PaymentStatus.DECLINED)
//                    .errorCode("PAYMENT_GATEWAY_ERROR")
//                    .errorMessage("Payment gateway error occurred")
//                    .build();
//        }

        return new PaymentResult.Builder()
                .transactionId(generateUniqueTransactionId())
                .status(PaymentStatus.APPROVED)
                .approvedAt(LocalDateTime.now())
                .build();
    }

    private String generateUniqueTransactionId() {
        return UUID.randomUUID().toString();
    }
}
