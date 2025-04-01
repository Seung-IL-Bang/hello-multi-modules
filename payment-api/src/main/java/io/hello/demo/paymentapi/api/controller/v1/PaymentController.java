package io.hello.demo.paymentapi.api.controller.v1;

import io.hello.demo.paymentapi.api.controller.v1.request.PaymentRequestDto;
import io.hello.demo.paymentapi.api.controller.v1.response.PaymentResultDto;
import io.hello.demo.paymentapi.domain.PaymentProcessor;
import io.hello.demo.paymentapi.domain.PaymentResult;
import io.hello.demo.paymentapi.support.response.ApiResponse;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PaymentController {

    private final PaymentProcessor paymentProcessor;

    public PaymentController(PaymentProcessor paymentProcessor) {
        this.paymentProcessor = paymentProcessor;
    }

    @PostMapping("/payment")
    public ApiResponse<PaymentResultDto> paymentPost(@RequestBody PaymentRequestDto requestDto) {
        PaymentResult result = paymentProcessor.process(requestDto.toPaymentData());
        PaymentResultDto responseDto = new PaymentResultDto(result);
        return ApiResponse.success(responseDto);
    }
}
