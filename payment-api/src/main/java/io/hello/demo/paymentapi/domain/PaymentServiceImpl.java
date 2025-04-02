package io.hello.demo.paymentapi.domain;

import io.hello.demo.inventoryapi.InventoryService;
import io.hello.demo.paymentapi.domain.generator.TransactionIdGenerator;
import io.hello.demo.paymentapi.domain.processor.PaymentProcessor;
import io.hello.demo.paymentapi.support.error.ErrorType;
import org.springframework.stereotype.Service;


@Service
public class PaymentServiceImpl implements PaymentService {

    private final TransactionIdGenerator transactionIdGenerator;
    private final InventoryService inventoryService;
    private final PaymentProcessor paymentProcessor;
    private final PaymentResultEventService paymentResultEventService;

    public PaymentServiceImpl(TransactionIdGenerator transactionIdGenerator,
                              PaymentProcessor paymentProcessor,
                              InventoryService inventoryService,
                              PaymentResultEventService paymentResultEventService) {
        this.transactionIdGenerator = transactionIdGenerator;
        this.paymentProcessor = paymentProcessor;
        this.inventoryService = inventoryService;
        this.paymentResultEventService = paymentResultEventService;
    }

    @Override
    public PaymentResult processPayment(PaymentContext paymentContext, String productId, int quantity) {
        String transactionId = transactionIdGenerator.generate();

        // Check and reserve inventory
        if (!inventoryService.checkAndReserveInventory(productId, quantity)) {
            PaymentResult result = createDeclinedResult(transactionId);
            paymentResultEventService.sendPaymentResultEvent(result);
            return result;
        }

        try {
            // Process payment
            PaymentResult result = paymentProcessor.process(paymentContext, transactionId);

            // Send payment result event
            paymentResultEventService.sendPaymentResultEvent(result);

            return result;
        } catch (Exception e) {
            // Rollback inventory reservation
            inventoryService.rollbackInventory(productId, quantity);

            PaymentResult result = createDeclinedResult(transactionId);
            paymentResultEventService.sendPaymentResultEvent(result);
            return result;
        }
    }

    private PaymentResult createDeclinedResult(String transactionId) {
        return new PaymentResult.Builder()
                .transactionId(transactionId)
                .status(PaymentStatus.DECLINED)
                .errorCode(ErrorType.INSUFFICIENT_INVENTORY.name())
                .errorMessage(ErrorType.INSUFFICIENT_INVENTORY.getMessage())
                .build();
    }
}
