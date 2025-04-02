package io.hello.demo.paymentapi.api.controller;

import io.hello.demo.paymentapi.api.controller.request.PaymentRequestDto;
import io.hello.demo.paymentapi.api.controller.response.PaymentResultDto;
import io.hello.demo.paymentapi.domain.PaymentContext;
import io.hello.demo.paymentapi.domain.PaymentResult;
import io.hello.demo.paymentapi.domain.PaymentService;
import io.hello.demo.paymentapi.domain.method.PaymentMethodType;
import io.hello.demo.paymentapi.domain.request.PaymentRequest;
import io.hello.demo.paymentapi.support.error.CoreException;
import io.hello.demo.paymentapi.support.error.ErrorType;
import io.hello.demo.paymentapi.support.response.ApiResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/v2/payment")
    public ApiResponse<PaymentResultDto> paymentPost(@RequestBody PaymentRequestDto request) {
        PaymentContext paymentContext = createPaymentContext(request);
        PaymentResult result = paymentService.processPayment(paymentContext, request.getProductId(), request.getQuantity());
        return ApiResponse.success(new PaymentResultDto(result));
    }

    private PaymentContext createPaymentContext(PaymentRequestDto request) {
        PaymentMethodType paymentMethodType = request.getPaymentMethodType();
        PaymentRequest paymentRequest = switch (paymentMethodType) {
            case CREDIT_CARD -> request.getCreditCardInfo().toPaymentRequest();
            case MOBILE -> request.getMobileInfo().toPaymentRequest();
            case VIRTUAL_ACCOUNT -> request.getVirtualAccountInfo().toPaymentRequest();
            default -> throw new CoreException(ErrorType.DEFAULT_ERROR); // todo: add error type
        };
        return new PaymentContext(paymentMethodType, paymentRequest);
    }
}
