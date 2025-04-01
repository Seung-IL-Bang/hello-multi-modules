package io.hello.demo.paymentapi.domain;

import io.hello.demo.inventoryapi.InventoryService;
import io.hello.demo.paymentapi.domain.generator.TransactionIdGenerator;
import io.hello.demo.paymentapi.domain.processor.PaymentProcessorV2;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;


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

        return paymentProcessor.process(paymentContext); // 결제 처리
    }
}
