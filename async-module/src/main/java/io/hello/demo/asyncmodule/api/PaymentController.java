package io.hello.demo.asyncmodule.api;

import io.hello.demo.asyncmodule.domain.PaymentService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/v1/payment/async")
    public String processPaymentAsync() {
        System.out.println(">>> [비동기식 결제 컨트롤러] 결제 요청");

        long start = System.currentTimeMillis();
        paymentService.processPaymentAsync(UUID.randomUUID().toString());
        long end = System.currentTimeMillis();

        long elapsed = end - start;
        System.out.println("<<< [비동기식 결제 컨트롤러] 결제 프로세스 소요 시간: " + elapsed + "ms");
        return "<<< [비동기식 결제 컨트롤러] 결제 프로세스 완료";
    }

    @PostMapping("/v1/payment/sync")
    public String processPaymentSync() {
        System.out.println(">>> [동기식 결제 컨트롤러] 결제 요청");
        long start = System.currentTimeMillis();
        paymentService.processPaymentSync(UUID.randomUUID().toString());
        long end = System.currentTimeMillis();

        long elapsed = end - start;
        System.out.println("<<< [동기식 결제 컨트롤러] 결제 프로세스 소요 시간: " + elapsed + "ms");
        return "<<< [동기식 결제 컨트롤러] 결제 프로세스 완료";
    }

    @PostMapping("/v1/payment/async/uncaught-exception")
    public String processPaymentAsyncWithUncaughtException() {
        System.out.println(">>> [비동기식 예외 미처리 결제 컨트롤러] 결제 요청");

        long start = System.currentTimeMillis();
        paymentService.processPaymentUncaughtAsyncException(UUID.randomUUID().toString());
        long end = System.currentTimeMillis();

        long elapsed = end - start;
        System.out.println("<<< [비동기식 예외 미처리 결제 컨트롤러] 결제 프로세스 소요 시간: " + elapsed + "ms");
        return "<<< [비동기식 예외 미처리 결제 컨트롤러] 결제 프로세스 완료";
    }

    @PostMapping("/v1/payment/async/caught-exception")
    public String processPaymentAsyncWithCaughtException() {
        System.out.println(">>> [비동기식 예외 처리 결제 컨트롤러] 결제 요청");

        long start = System.currentTimeMillis();
        paymentService.processPaymentCaughtAsyncException(UUID.randomUUID().toString());
        long end = System.currentTimeMillis();

        long elapsed = end - start;
        System.out.println("<<< [비동기식 예외 처리 결제 컨트롤러] 결제 프로세스 소요 시간: " + elapsed + "ms");
        return "<<< [비동기식 예외 처리 결제 컨트롤러] 결제 프로세스 완료";
    }
}
