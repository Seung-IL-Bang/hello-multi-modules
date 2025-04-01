package io.hello.demo.paymentapi.domain.v3;

import io.hello.demo.paymentapi.domain.PaymentProcessor;
import io.hello.demo.paymentapi.domain.PaymentRequest;
import io.hello.demo.paymentapi.domain.PaymentResult;
import io.hello.demo.paymentapi.domain.PaymentStatus;
import io.hello.demo.paymentapi.domain.v3.generator.TransactionIdGenerator;
import io.hello.demo.paymentapi.domain.v3.validator.PaymentValidator;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class DefaultPaymentProcessorV3 implements PaymentProcessor {

    private final TransactionIdGenerator transactionIdGenerator;
    private final List<PaymentValidator> validators;

    public DefaultPaymentProcessorV3(TransactionIdGenerator transactionIdGenerator, List<PaymentValidator> validators) {
        this.transactionIdGenerator = transactionIdGenerator;
        this.validators = validators;
    }

    @Override
    public PaymentResult process(PaymentRequest request) {
        validators.forEach(validator -> validator.validate(request));
        return new PaymentResult.Builder()
                .transactionId(transactionIdGenerator.generate())
                .status(PaymentStatus.APPROVED)
                .approvedAt(LocalDateTime.now())
                .build();
    }
}
