package io.hello.demo.paymentapi.domain.processor.v2;

import io.hello.demo.paymentapi.domain.processor.PaymentProcessor;
import io.hello.demo.paymentapi.domain.PaymentRequest;
import io.hello.demo.paymentapi.domain.PaymentResult;
import io.hello.demo.paymentapi.domain.PaymentStatus;
import io.hello.demo.paymentapi.domain.validator.PaymentValidator;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class DefaultPaymentProcessorV2 implements PaymentProcessor {

    private final List<PaymentValidator> validators;

    public DefaultPaymentProcessorV2(List<PaymentValidator> validators) {
        this.validators = validators;
    }

    @Override
    public PaymentResult process(PaymentRequest request) {
        validators.forEach(validator -> validator.validate(request));

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
