package io.hello.demo.paymentapi.domain.method.validator.virtualaccount;

import io.hello.demo.paymentapi.domain.request.PaymentRequest;
import io.hello.demo.paymentapi.domain.request.VirtualAccountPaymentRequest;
import io.hello.demo.paymentapi.domain.method.validator.PaymentMethodValidator;
import io.hello.demo.paymentapi.support.error.CoreException;
import io.hello.demo.paymentapi.support.error.ErrorType;

public class VirtualAccountHolderNameValidator implements PaymentMethodValidator {
    @Override
    public void validate(PaymentRequest request) {
        if (request instanceof VirtualAccountPaymentRequest virtualAccountPaymentRequest) {
            if (virtualAccountPaymentRequest.holderName().isBlank()) {
                throw new CoreException(ErrorType.INVALID_PAYMENT_VIRTUAL_ACCOUNT_HOLDER_NAME);
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
