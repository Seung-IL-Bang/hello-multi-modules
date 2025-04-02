package io.hello.demo.paymentapi.domain;

import io.hello.demo.inventoryapi.DefaultInventoryService;
import io.hello.demo.inventoryapi.InventoryService;
import io.hello.demo.paymentapi.domain.generator.TransactionIdGenerator;
import io.hello.demo.paymentapi.domain.generator.UuidTransactionIdGenerator;
import io.hello.demo.paymentapi.domain.method.*;
import io.hello.demo.paymentapi.domain.processor.PaymentProcessorV2;
import io.hello.demo.paymentapi.domain.processor.v4.DefaultPaymentProcessorV4;
import io.hello.demo.paymentapi.domain.request.v2.CreditCardPaymentRequest;
import io.hello.demo.paymentapi.domain.validator.v2.creditcard.AmountValidator;
import io.hello.demo.paymentapi.domain.validator.v2.creditcard.CardCvcValidator;
import io.hello.demo.paymentapi.domain.validator.v2.creditcard.CardExpiryValidator;
import io.hello.demo.paymentapi.domain.validator.v2.creditcard.CardNumberValidator;
import io.hello.demo.paymentapi.domain.validator.v2.PaymentMethodValidator;
import io.hello.demo.sseapi.SseService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;

class PaymentServiceTest {

    private static final Logger log = LoggerFactory.getLogger(PaymentServiceTest.class);

    private InventoryService inventoryService;

    private PaymentService paymentService;

    @BeforeEach
    void setUp() {
        TransactionIdGenerator transactionIdGenerator = new UuidTransactionIdGenerator();

//        List<PaymentValidator> validators = List.of(
//                new AmountValidator(),
//                new CardNumberValidator(),
//                new CardExpiryValidator(),
//                new CardCvcValidator()
//        );

        List<PaymentMethodValidator> validators = List.of(
                new AmountValidator(),
                new CardNumberValidator(),
                new CardExpiryValidator(),
                new CardCvcValidator()
        );

        List<PaymentMethod> paymentMethods = List.of(
                new CreditCardPaymentMethod(validators, transactionIdGenerator),
                new VirtualAccountPaymentMethod(),
                new MobilePaymentMethod()
        );

        PaymentMethodFactory paymentMethodFactory = new PaymentMethodFactory(paymentMethods);

        inventoryService = new DefaultInventoryService();
//        PaymentProcessor paymentProcessor = new DefaultPaymentProcessorV3(transactionIdGenerator, validators);

        PaymentProcessorV2 paymentProcessor = new DefaultPaymentProcessorV4(paymentMethodFactory);
        paymentService = new PaymentServiceImpl(transactionIdGenerator, paymentProcessor, inventoryService);
    }

    @DisplayName("재고가 남아 있는 상품에 대해 결제 승인 처리될 경우, 유저에게 결제 승인 알림이 전송된다.")
    @Test
    void testPaymentProcess_withAvailableInventory_shouldSendApprovalNotification() {
        // given
        String productId = "product-1";
        int quantity = 1;

        PaymentContext paymentContext = new PaymentContext(
                PaymentMethodType.CREDIT_CARD,
                new CreditCardPaymentRequest(
                        1000L,
                        "1234-5678-9012-3456",
                        "12/25",
                        "123",
                        "merchant-1"
                )
        );

        // when
        PaymentResult result = paymentService.processPayment(paymentContext, productId, quantity);

        // then
        assertThat(result.status()).isEqualTo(PaymentStatus.APPROVED);
    }

    @DisplayName("재고가 50개인 상품을 100번의 동시 결제 요청이 들어오면 정확히 50번만 성공하고 남은 재고는 0이어야 한다.")
    @Test
    void testConcurrentPaymentProcess_with100Requests_shouldSuccess50Times() {
        // given
        int initialInventory = 50;
        String productId = "product-2";
        int numConcurrentRequests = 100;

        try {
            ExecutorService es = Executors.newFixedThreadPool(100);

            CountDownLatch readyLatch = new CountDownLatch(numConcurrentRequests); // 모든 스레드가 준비됐는지 확인
            CountDownLatch startLatch = new CountDownLatch(1); // 모든 스레드가 동시에 시작하게 하는 신호용
            CountDownLatch finishLatch = new CountDownLatch(numConcurrentRequests); // 모든 스레드가 종료됐는지 확인
            AtomicInteger successCount = new AtomicInteger(0);
            AtomicInteger failedCount = new AtomicInteger(0);

            // when
            for (int i = 0; i < numConcurrentRequests; i++) {
                final int count = i;
                es.submit(() -> {
                    PaymentContext paymentContext = new PaymentContext(
                            PaymentMethodType.CREDIT_CARD,
                            new CreditCardPaymentRequest(
                                    1000L,
                                    "1234-5678-9012-3456",
                                    "12/25",
                                    "123",
                                    "merchant-1"
                            )
                    );

                    try {
                        log.info("Processing payment request Thread-{}", count);

                        readyLatch.countDown();
                        startLatch.await();
                        PaymentResult result = paymentService.processPayment(paymentContext, productId, 1);
                        if (result.status() == PaymentStatus.APPROVED) {
                            successCount.incrementAndGet();
                            log.info("Payment approved Thread-{} : {}", count, result.transactionId());
                        } else {
                            failedCount.incrementAndGet();
                            log.info("Payment declined Thread-{} : {}", count, result.errorCode());
                        }
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    } finally {
                        finishLatch.countDown();
                    }
                });
            }

            readyLatch.await();
            startLatch.countDown();
            finishLatch.await();
            es.shutdown();

            log.info("성공 횟수: {}", successCount.get());
            log.info("실패 횟수: {}", failedCount.get());
            log.info("남은 재고: {}", inventoryService.getInventory(productId));

            // then
            assertThat(successCount.get()).isEqualTo(initialInventory);
            assertThat(failedCount.get()).isEqualTo(numConcurrentRequests - initialInventory);
            assertThat(inventoryService.getInventory(productId)).isEqualTo(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}