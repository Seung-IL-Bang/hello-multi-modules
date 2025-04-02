package io.hello.demo.paymentapi.domain.method;

import io.hello.demo.paymentapi.domain.PaymentResult;
import io.hello.demo.paymentapi.domain.PaymentStatus;
import io.hello.demo.paymentapi.domain.method.validator.PaymentMethodValidatorFactory;
import io.hello.demo.paymentapi.domain.request.PaymentRequest;
import io.hello.demo.paymentapi.support.error.CoreException;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class VirtualAccountPaymentMethod implements PaymentMethod {

    private final PaymentMethodValidatorFactory validatorFactory;

    public VirtualAccountPaymentMethod(PaymentMethodValidatorFactory paymentMethodValidatorFactory) {
        this.validatorFactory = paymentMethodValidatorFactory;
    }

    @Override
    public void validate(PaymentRequest request) {
        validatorFactory
                .getPaymentMethodValidator(request)
                .forEach(validator -> validator.validate(request));
    }

    @Override
    public PaymentResult pay(PaymentRequest request, String transactionId) {
        try {
            validate(request);
        } catch (CoreException e) {
            return new PaymentResult.Builder()
                    .transactionId(transactionId)
                    .status(PaymentStatus.DECLINED)
                    .errorCode(e.getErrorType().getCode().name())
                    .errorMessage(e.getMessage())
                    .build();
        }

        return new PaymentResult.Builder()
                .transactionId(transactionId)
                .status(PaymentStatus.APPROVED)
                .approvedAt(LocalDateTime.now())
                .build();
    }

    @Override
    public boolean supports(PaymentMethodType type) {
        return PaymentMethodType.VIRTUAL_ACCOUNT.equals(type);
    }
}
