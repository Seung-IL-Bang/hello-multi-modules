package io.hello.demo.paymentapi.domain.validator.v2.creditcard;

import io.hello.demo.paymentapi.domain.request.v2.CreditCardPaymentRequest;
import io.hello.demo.paymentapi.domain.request.v2.PaymentRequest;
import io.hello.demo.paymentapi.domain.validator.v2.PaymentMethodValidator;
import io.hello.demo.paymentapi.support.error.CoreException;
import io.hello.demo.paymentapi.support.error.ErrorType;

import java.util.regex.Pattern;

public class CardExpiryValidator implements PaymentMethodValidator {

    private static final Pattern CARD_EXPIRY_PATTERN = Pattern.compile("^(0[1-9]|1[0-2])/\\d{2}$");

    @Override
    public void validate(PaymentRequest request) {
        if (request instanceof CreditCardPaymentRequest creditCardPaymentRequest) {
            if (!CARD_EXPIRY_PATTERN.matcher(creditCardPaymentRequest.cardExpiry()).matches()) {
                throw new CoreException(ErrorType.INVALID_PAYMENT_CARD_EXPIRY);
            }
        } else {
            throw new CoreException(ErrorType.DEFAULT_ERROR); // todo add error type
        }
    }
}
