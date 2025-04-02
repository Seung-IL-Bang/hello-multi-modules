package io.hello.demo.paymentapi.api.controller;

import io.hello.demo.paymentapi.api.config.PaymentContextFactory;
import io.hello.demo.paymentapi.api.controller.request.PaymentRequestDto;
import io.hello.demo.paymentapi.api.controller.response.PaymentResultDto;
import io.hello.demo.paymentapi.domain.PaymentContext;
import io.hello.demo.paymentapi.domain.PaymentResult;
import io.hello.demo.paymentapi.domain.PaymentService;
import io.hello.demo.paymentapi.support.response.ApiResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PaymentController {

    private final PaymentService paymentService;
    private final PaymentContextFactory paymentContextFactory;

    public PaymentController(PaymentService paymentService,
                             PaymentContextFactory paymentContextFactory) {
        this.paymentService = paymentService;
        this.paymentContextFactory = paymentContextFactory;
    }

    @PostMapping("/api/v1/payment")
    public ApiResponse<PaymentResultDto> paymentPost(@RequestBody PaymentRequestDto request) {
        PaymentContext paymentContext = paymentContextFactory.createPaymentContext(request);
        PaymentResult result = paymentService.processPayment(paymentContext, request.getProductId(), request.getQuantity());
        return ApiResponse.success(new PaymentResultDto(result));
    }
}
