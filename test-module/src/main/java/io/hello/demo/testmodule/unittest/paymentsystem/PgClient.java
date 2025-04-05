package io.hello.demo.testmodule.unittest.paymentsystem;

import io.hello.demo.testmodule.unittest.paymentsystem.PaymentRequest.CardInfo;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public interface PgClient {
    String requestPayment(String paymentId, BigDecimal amount, CardInfo cardInfo);
    String cancelPayment(String paymentId, String pgTraceId, BigDecimal amount);
}
