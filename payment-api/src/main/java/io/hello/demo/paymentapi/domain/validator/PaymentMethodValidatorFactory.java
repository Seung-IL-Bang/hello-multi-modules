package io.hello.demo.paymentapi.domain.validator;

import io.hello.demo.paymentapi.domain.request.PaymentRequest;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PaymentMethodValidatorFactory {

    private final List<PaymentMethodValidator> paymentMethodValidators;

    public PaymentMethodValidatorFactory(List<PaymentMethodValidator> paymentMethodValidators) {
        this.paymentMethodValidators = paymentMethodValidators;
    }

    public List<PaymentMethodValidator> getPaymentMethodValidator(PaymentRequest request) {
        return paymentMethodValidators.stream()
                .filter(paymentMethodValidator -> paymentMethodValidator.supports(request))
                .toList();
    }
}
