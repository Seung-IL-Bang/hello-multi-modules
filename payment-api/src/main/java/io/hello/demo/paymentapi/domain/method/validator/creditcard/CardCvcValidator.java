package io.hello.demo.paymentapi.domain.method.validator.creditcard;

import io.hello.demo.paymentapi.domain.request.CreditCardPaymentRequest;
import io.hello.demo.paymentapi.domain.request.PaymentRequest;
import io.hello.demo.paymentapi.domain.method.validator.PaymentMethodValidator;
import io.hello.demo.paymentapi.support.error.CoreException;
import io.hello.demo.paymentapi.support.error.ErrorType;

import java.util.regex.Pattern;

public class CardCvcValidator implements PaymentMethodValidator {

    private static final Pattern CARD_CVC_PATTERN = Pattern.compile("^\\d{3}$");

    @Override
    public void validate(PaymentRequest request) {
        if (request instanceof CreditCardPaymentRequest creditCardPaymentRequest) {
            if (!CARD_CVC_PATTERN.matcher(creditCardPaymentRequest.cardCvc()).matches()) {
                throw new CoreException(ErrorType.INVALID_PAYMENT_CARD_CVC);
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
