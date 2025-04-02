package io.hello.demo.paymentapi.domain.method.validator.virtualaccount;

import io.hello.demo.paymentapi.domain.request.PaymentRequest;
import io.hello.demo.paymentapi.domain.request.VirtualAccountPaymentRequest;
import io.hello.demo.paymentapi.domain.method.validator.PaymentMethodValidator;
import io.hello.demo.paymentapi.support.error.CoreException;
import io.hello.demo.paymentapi.support.error.ErrorType;

public class VirtualAccountNumberValidator implements PaymentMethodValidator {
    @Override
    public void validate(PaymentRequest request) {
        if (request instanceof VirtualAccountPaymentRequest virtualAccountPaymentRequest) {
            if (virtualAccountPaymentRequest.accountNumber().length() != 10) {
                throw new CoreException(ErrorType.INVALID_PAYMENT_VIRTUAL_ACCOUNT_NUMBER);
            }
        } else {
            throw new CoreException(ErrorType.DEFAULT_ERROR); // todo add error type
        }
    }

    @Override
    public boolean supports(PaymentRequest request) {
        return request instanceof VirtualAccountPaymentRequest;
    }
}
