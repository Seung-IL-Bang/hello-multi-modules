package io.hello.demo.paymentapi.domain.v3.validator;

import io.hello.demo.paymentapi.domain.PaymentRequest;
import io.hello.demo.paymentapi.support.error.CoreException;
import io.hello.demo.paymentapi.support.error.ErrorType;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@Component
public class CardNumberValidator implements PaymentValidator {

    private static final Pattern CARD_NUMBER_PATTERN = Pattern.compile("^\\d{4}-\\d{4}-\\d{4}-\\d{4}$");

    @Override
    public void validate(PaymentRequest request) {
        if (!CARD_NUMBER_PATTERN.matcher(request.cardNumber()).matches()) {
            throw new CoreException(ErrorType.INVALID_PAYMENT_CARD_NUMBER);
        }
    }
}
