package io.hello.demo.paymentapi.domain;

import io.hello.demo.sseapi.PaymentResultEvent;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;

@Service
public class PaymentResultEventService {

    private final RestTemplate restTemplate;

    public PaymentResultEventService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Async
    public void sendPaymentResultEvent(PaymentResult paymentResult) {
        String url = "http://localhost:8080/api/v1/sse/send/payment-result";

        if (paymentResult.status() == PaymentStatus.APPROVED) {
            PaymentResultEvent paymentResultEvent = new PaymentResultEvent(
                    "user-1",
                    paymentResult.transactionId(),
                    true,
                    PaymentStatus.APPROVED.name(),
                    LocalDateTime.now().toString()
            );
            restTemplate.postForObject(url, paymentResultEvent, Void.class);
        } else {
            PaymentResultEvent paymentResultEvent = new PaymentResultEvent(
                    "user-1",
                    paymentResult.transactionId(),
                    false,
                    PaymentStatus.DECLINED.name(),
                    LocalDateTime.now().toString()
            );
            restTemplate.postForObject(url, paymentResultEvent, Void.class);
        }
    }

}
