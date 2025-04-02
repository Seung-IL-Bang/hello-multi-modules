package io.hello.demo.paymentapi.api.config;

import io.hello.demo.paymentapi.api.controller.request.PaymentRequestDto;
import io.hello.demo.paymentapi.domain.PaymentContext;
import io.hello.demo.paymentapi.domain.method.PaymentMethodType;
import io.hello.demo.paymentapi.domain.request.PaymentRequest;
import io.hello.demo.paymentapi.support.error.CoreException;
import io.hello.demo.paymentapi.support.error.ErrorType;

import java.util.EnumMap;
import java.util.Map;
import java.util.function.Function;

public interface PaymentContextFactory {

    PaymentContext createPaymentContext(PaymentRequestDto requestDto);

    class DefaultPaymentContextFactory implements PaymentContextFactory {

        private final Map<PaymentMethodType, Function<PaymentRequestDto, PaymentRequest>> requestCreators;

        public DefaultPaymentContextFactory() {
            this.requestCreators = new EnumMap<>(PaymentMethodType.class);
            initializeRequestCreators();
        }

        private void initializeRequestCreators() {
            requestCreators.put(PaymentMethodType.CREDIT_CARD, requestDto -> requestDto.getCreditCardInfo().toPaymentRequest());
            requestCreators.put(PaymentMethodType.VIRTUAL_ACCOUNT, requestDto -> requestDto.getVirtualAccountInfo().toPaymentRequest());
            requestCreators.put(PaymentMethodType.MOBILE, requestDto -> requestDto.getMobileInfo().toPaymentRequest());
            // New payment methods can be added here without modifying controller code
        }

        @Override
        public PaymentContext createPaymentContext(PaymentRequestDto requestDto) {
            PaymentMethodType paymentMethodType = requestDto.getPaymentMethodType();
            Function<PaymentRequestDto, PaymentRequest> requestCreator = requestCreators.get(paymentMethodType);

            if (requestCreator == null) {
                throw new CoreException(ErrorType.DEFAULT_ERROR); // todo: add error type
            }

            PaymentRequest paymentRequest = requestCreator.apply(requestDto);
            return new PaymentContext(paymentMethodType, paymentRequest);
        }
    }

}
