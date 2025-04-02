package io.hello.demo.paymentapi.domain.method.validator.creditcard;

import io.hello.demo.paymentapi.domain.request.CreditCardPaymentRequest;
import io.hello.demo.paymentapi.domain.request.PaymentRequest;
import io.hello.demo.paymentapi.domain.method.validator.PaymentMethodValidator;
import io.hello.demo.paymentapi.support.error.CoreException;
import io.hello.demo.paymentapi.support.error.ErrorType;

public class CardAmountValidator implements PaymentMethodValidator {
    @Override
    public void validate(PaymentRequest request) {
        if (request instanceof CreditCardPaymentRequest creditCardPaymentRequest) {
            if (creditCardPaymentRequest.amount() < 0) {
                throw new CoreException(ErrorType.INVALID_PAYMENT_AMOUNT);
            }
        } else {
            throw new CoreException(ErrorType.DEFAULT_ERROR); // todo add error type
        }
    }

    @Override
    public boolean supports(PaymentRequest request) {
        return request instanceof CreditCardPaymentRequest;
    }
}
