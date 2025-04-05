package io.hello.demo.testmodule.paymentsystem;

import io.hello.demo.testmodule.paymentsystem.domain.AccountInfo;
import io.hello.demo.testmodule.paymentsystem.domain.CardInfo;
import io.hello.demo.testmodule.paymentsystem.domain.PaymentRequest;
import io.hello.demo.testmodule.paymentsystem.domain.TossPayInfo;

import java.math.BigDecimal;

public class PaymentRequestFixture {

    public static PaymentRequest createPaymentRequest(String paymentMethodType) {
        PaymentRequest paymentRequest = createPaymentRequest();
        paymentRequest.setPaymentMethodType(paymentMethodType);
        return paymentRequest;
    }

    public static PaymentRequest createPaymentRequest() {
        PaymentRequest request = new PaymentRequest();
        request.setAmount(new BigDecimal("100.00"));
        request.setCardInfo(createCardInfo());
        request.setAccountInfo(createAccountInfo());
        request.setTossPayInfo(createTossPayInfo());
        return request;
    }

    private static CardInfo createCardInfo() {
        CardInfo cardInfo = new CardInfo();
        cardInfo.setCardNumber("4111111111111111");
        cardInfo.setExpiryDate("12/25");
        cardInfo.setCvv("123");
        return cardInfo;
    }

    private static AccountInfo createAccountInfo() {
        AccountInfo accountInfo = new AccountInfo();
        accountInfo.setBankCode("XYZ");
        accountInfo.setAccountNumber("1234567890");
        return accountInfo;
    }

    private static TossPayInfo createTossPayInfo() {
        TossPayInfo tossPayInfo = new TossPayInfo();
        tossPayInfo.setTossPayToken("sample-token");
        return tossPayInfo;
    }
}
