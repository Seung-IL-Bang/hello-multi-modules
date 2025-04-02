package io.hello.demo.paymentapi.domain;

import io.hello.demo.inventoryapi.InventoryService;
import io.hello.demo.paymentapi.domain.generator.TransactionIdGenerator;
import io.hello.demo.paymentapi.domain.processor.PaymentProcessorV2;
import io.hello.demo.sseapi.PaymentResultEvent;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;


@Service
public class PaymentServiceImpl implements PaymentService {

    private final TransactionIdGenerator transactionIdGenerator;
    private final PaymentProcessorV2 paymentProcessor;
    private final InventoryService inventoryService;

    public PaymentServiceImpl(TransactionIdGenerator transactionIdGenerator,
                              @Qualifier("defaultPaymentProcessorV4") PaymentProcessorV2 paymentProcessor,
                              InventoryService inventoryService) {
        this.transactionIdGenerator = transactionIdGenerator;
        this.paymentProcessor = paymentProcessor;
        this.inventoryService = inventoryService;
    }


    @Override
    public PaymentResult processPayment(PaymentContext paymentContext, String productId, int quantity) {

        boolean inventoryReserved = inventoryService.checkAndReserveInventory(productId, quantity);

        if (!inventoryReserved) {
            return new PaymentResult.Builder()
                    .transactionId(transactionIdGenerator.generate())
                    .status(PaymentStatus.DECLINED)
                    .errorCode("INSUFFICIENT_INVENTORY")
                    .errorMessage("Insufficient inventory")
                    .build();
        }

        PaymentResult result = paymentProcessor.process(paymentContext);

        RestTemplate restTemplate = new RestTemplate();
        String url = "http://localhost:8080/api/v1/sse/send/payment-result";

        if (result.status() == PaymentStatus.APPROVED) {
            PaymentResultEvent paymentResultEvent = new PaymentResultEvent(
                    "user-1",
                    result.transactionId(),
                    true,
                    PaymentStatus.APPROVED.name(),
                    LocalDateTime.now().toString()
            );
            restTemplate.postForObject(url, paymentResultEvent, Void.class);
        } else {
            PaymentResultEvent paymentResultEvent = new PaymentResultEvent(
                    "user-1",
                    result.transactionId(),
                    false,
                    PaymentStatus.DECLINED.name(),
                    LocalDateTime.now().toString()
            );
            restTemplate.postForObject(url, paymentResultEvent, Void.class);
        }

        return result; // 결제 처리
    }
}
