package io.hello.demo.testmodule.paymentsystem.domain.processor;

import io.hello.demo.testmodule.paymentsystem.domain.PaymentRequest;
import io.hello.demo.testmodule.paymentsystem.domain.PaymentResult;

import java.math.BigDecimal;

public class SecurityPaymentDecorator extends PaymentProcessorDecorator {
    public SecurityPaymentDecorator(PaymentProcessor processor) {
        super(processor);
    }

    @Override
    public PaymentResult processPayment(PaymentRequest request) {
        // 결제 요청 보안 검증
        validateSecurity(request);
        return processor.processPayment(request);
    }

    @Override
    public PaymentResult cancelPayment(String paymentId, BigDecimal amount) {
        // 취소 요청 보안 검증
        validateCancellationSecurity(paymentId, amount);
        return processor.cancelPayment(paymentId, amount);
    }

    private void validateSecurity(PaymentRequest request) {
        // 예: IP 검증, 사기 거래 탐지, 이상 거래 탐지 등
        System.out.println("SECURITY: Validating payment request for order " + request.getOrderId());

        // 실제로는 더 복잡한 보안 검증 로직이 들어갈 것
        if (request.getAmount().compareTo(new BigDecimal("10000000")) > 0) {
            System.out.println("SECURITY: Large amount detected, additional verification required");
            // 추가 검증 로직
        }
    }

    private void validateCancellationSecurity(String paymentId, BigDecimal amount) {
        // 예: 취소 권한 검증, 취소 한도 검증 등
        System.out.println("SECURITY: Validating payment cancellation for payment " + paymentId);

        // 실제로는 더 복잡한 보안 검증 로직이 들어갈 것
        if (amount.compareTo(new BigDecimal("1000000")) > 0) {
            System.out.println("SECURITY: Large cancellation amount detected, additional verification required");
            // 추가 검증 로직
        }
    }
}
