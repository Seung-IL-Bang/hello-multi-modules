package io.hello.demo.paymentapi.domain.validator;

import io.hello.demo.paymentapi.domain.PaymentRequest;
import io.hello.demo.paymentapi.support.error.CoreException;
import io.hello.demo.paymentapi.support.error.ErrorType;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@Component
public class CardExpiryValidator implements PaymentValidator {

    private static final Pattern CARD_EXPIRY_PATTERN = Pattern.compile("^(0[1-9]|1[0-2])/\\d{2}$");

    @Override
    public void validate(PaymentRequest request) {
        if (!CARD_EXPIRY_PATTERN.matcher(request.cardExpiry()).matches()) {
            throw new CoreException(ErrorType.INVALID_PAYMENT_CARD_EXPIRY);
        }
    }
}
