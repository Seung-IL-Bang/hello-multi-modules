package io.hello.demo.paymentapi.domain;

import io.hello.demo.inventoryapi.DefaultInventoryService;
import io.hello.demo.inventoryapi.InventoryService;
import io.hello.demo.paymentapi.domain.generator.TransactionIdGenerator;
import io.hello.demo.paymentapi.domain.generator.UuidTransactionIdGenerator;
import io.hello.demo.paymentapi.domain.method.CreditCardPaymentMethod;
import io.hello.demo.paymentapi.domain.method.PaymentMethod;
import io.hello.demo.paymentapi.domain.method.PaymentMethodFactory;
import io.hello.demo.paymentapi.domain.method.PaymentMethodType;
import io.hello.demo.paymentapi.domain.processor.DefaultPaymentProcessor;
import io.hello.demo.paymentapi.domain.processor.PaymentProcessor;
import io.hello.demo.paymentapi.domain.request.CreditCardPaymentRequest;
import io.hello.demo.paymentapi.domain.method.validator.PaymentMethodValidatorFactory;
import io.hello.demo.paymentapi.domain.method.validator.creditcard.CardAmountValidator;
import io.hello.demo.paymentapi.domain.method.validator.creditcard.CardCvcValidator;
import io.hello.demo.paymentapi.domain.method.validator.creditcard.CardExpiryValidator;
import io.hello.demo.paymentapi.domain.method.validator.creditcard.CardNumberValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

class ConcurrentPaymentServiceTest {

    private static final Logger log = LoggerFactory.getLogger(ConcurrentPaymentServiceTest.class);

    private InventoryService inventoryService;

    private PaymentService paymentService;

    @BeforeEach
    void setUp() {
        TransactionIdGenerator transactionIdGenerator = new UuidTransactionIdGenerator();

        PaymentMethodValidatorFactory creditCardMethodValidatorFactory = new PaymentMethodValidatorFactory(List.of(
                new CardNumberValidator(),
                new CardCvcValidator(),
                new CardExpiryValidator(),
                new CardAmountValidator()
        ));

        List<PaymentMethod> paymentMethods = List.of(
                new CreditCardPaymentMethod(creditCardMethodValidatorFactory)
        );

        PaymentMethodFactory paymentMethodFactory = new PaymentMethodFactory(paymentMethods);

        inventoryService = new DefaultInventoryService();

        RestTemplate restTemplateMock = Mockito.mock(RestTemplate.class);
        when(restTemplateMock.postForObject(Mockito.anyString(), Mockito.any(), Mockito.any())).thenReturn(null);

        PaymentResultEventService paymentResultEventService = new PaymentResultEventService(restTemplateMock);

        PaymentProcessor paymentProcessor = new DefaultPaymentProcessor(paymentMethodFactory);
        paymentService = new PaymentServiceImpl(transactionIdGenerator, paymentProcessor, inventoryService, paymentResultEventService);
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