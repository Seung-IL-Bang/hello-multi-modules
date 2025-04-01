package io.hello.demo.paymentapi.domain.v2;

import io.hello.demo.paymentapi.domain.PaymentRequest;
import io.hello.demo.paymentapi.support.error.CoreException;
import io.hello.demo.paymentapi.support.error.ErrorType;
import org.springframework.stereotype.Component;

@Component
public class AmountValidator implements PaymentValidator{
    @Override
    public void validate(PaymentRequest request) {
        if (request.amount() < 0) {
            throw new CoreException(ErrorType.INVALID_PAYMENT_AMOUNT);
        }
    }
}
