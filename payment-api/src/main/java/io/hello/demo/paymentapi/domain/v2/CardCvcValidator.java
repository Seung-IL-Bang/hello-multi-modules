package io.hello.demo.paymentapi.domain.v2;

import io.hello.demo.paymentapi.domain.PaymentRequest;
import io.hello.demo.paymentapi.support.error.CoreException;
import io.hello.demo.paymentapi.support.error.ErrorType;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@Component
public class CardCvcValidator implements PaymentValidator {

    private static final Pattern CARD_CVC_PATTERN = Pattern.compile("^\\d{3}$");

    @Override
    public void validate(PaymentRequest request) {
        if (!CARD_CVC_PATTERN.matcher(request.cardCvc()).matches()) {
            throw new CoreException(ErrorType.INVALID_PAYMENT_CARD_CVC);
        }
    }
}
