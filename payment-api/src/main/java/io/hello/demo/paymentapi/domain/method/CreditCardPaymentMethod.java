package io.hello.demo.paymentapi.domain.method;

import io.hello.demo.paymentapi.domain.PaymentStatus;
import io.hello.demo.paymentapi.domain.generator.TransactionIdGenerator;
import io.hello.demo.paymentapi.domain.request.v2.PaymentRequest;
import io.hello.demo.paymentapi.domain.PaymentResult;
import io.hello.demo.paymentapi.domain.validator.v2.PaymentMethodValidator;
import io.hello.demo.paymentapi.support.error.CoreException;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class CreditCardPaymentMethod implements PaymentMethod {

    private final List<PaymentMethodValidator> validators;
    private final TransactionIdGenerator transactionIdGenerator;

    public CreditCardPaymentMethod(List<PaymentMethodValidator> validators,
                                   TransactionIdGenerator transactionIdGenerator) {
        this.validators = validators;
        this.transactionIdGenerator = transactionIdGenerator;
    }

    @Override
    public boolean validate(PaymentRequest request) {
        validators.forEach(validator -> validator.validate(request));
        return true;
    }

    @Override
    public PaymentResult execute(PaymentRequest request) {
        try {
            validate(request);
        } catch (CoreException e) {
            return new PaymentResult.Builder()
                    .transactionId(transactionIdGenerator.generate())
                    .status(PaymentStatus.DECLINED)
                    .errorCode(e.getErrorType().getCode().name())
                    .errorMessage(e.getMessage())
                    .build();
        }

        return new PaymentResult.Builder()
                .transactionId(transactionIdGenerator.generate())
                .status(PaymentStatus.APPROVED)
                .approvedAt(LocalDateTime.now())
                .build();
    }

    @Override
    public boolean supports(PaymentMethodType type) {
        return PaymentMethodType.CREDIT_CARD.equals(type);
    }
}
