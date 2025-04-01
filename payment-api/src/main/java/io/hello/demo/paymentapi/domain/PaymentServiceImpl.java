package io.hello.demo.paymentapi.domain;

import io.hello.demo.inventoryapi.InventoryService;
import io.hello.demo.paymentapi.domain.generator.TransactionIdGenerator;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class PaymentServiceImpl implements PaymentService {

    private final TransactionIdGenerator transactionIdGenerator;
    private final PaymentProcessor paymentProcessor;
    private final InventoryService inventoryService;

    public PaymentServiceImpl(TransactionIdGenerator transactionIdGenerator,
                              @Qualifier("defaultPaymentProcessorV3") PaymentProcessor paymentProcessor,
                              InventoryService inventoryService) {
        this.transactionIdGenerator = transactionIdGenerator;
        this.paymentProcessor = paymentProcessor;
        this.inventoryService = inventoryService;
    }


    @Override
    public PaymentResult processPayment(PaymentRequest request, String productId, int quantity) {

        boolean inventoryReserved = inventoryService.checkAndReserveInventory(productId, quantity);

        if (!inventoryReserved) {
            return new PaymentResult.Builder()
                    .transactionId(transactionIdGenerator.generate())
                    .status(PaymentStatus.DECLINED)
                    .errorCode("INSUFFICIENT_INVENTORY")
                    .errorMessage("Insufficient inventory")
                    .build();
        }

        return paymentProcessor.process(request); // 결제 처리
    }
}
