package io.hello.demo.paymentapi.domain;

import io.hello.demo.paymentapi.domain.generator.TransactionIdGenerator;
import io.hello.demo.paymentapi.domain.generator.UuidTransactionIdGenerator;
import io.hello.demo.paymentapi.domain.method.PaymentMethod;
import io.hello.demo.paymentapi.domain.method.PaymentMethodFactory;
import io.hello.demo.paymentapi.domain.method.PaymentMethodType;
import io.hello.demo.paymentapi.domain.method.VirtualAccountPaymentMethod;
import io.hello.demo.paymentapi.domain.method.validator.PaymentMethodValidator;
import io.hello.demo.paymentapi.domain.method.validator.PaymentMethodValidatorFactory;
import io.hello.demo.paymentapi.domain.method.validator.virtualaccount.VirtualAccountAmountValidator;
import io.hello.demo.paymentapi.domain.method.validator.virtualaccount.VirtualAccountBankCodeValidator;
import io.hello.demo.paymentapi.domain.method.validator.virtualaccount.VirtualAccountHolderNameValidator;
import io.hello.demo.paymentapi.domain.method.validator.virtualaccount.VirtualAccountNumberValidator;
import io.hello.demo.paymentapi.domain.processor.DefaultPaymentProcessor;
import io.hello.demo.paymentapi.domain.processor.PaymentProcessor;
import io.hello.demo.paymentapi.domain.request.VirtualAccountPaymentRequest;
import io.hello.demo.paymentapi.support.error.ErrorType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class VirtualAccountPaymentMethodTest {

    private TransactionIdGenerator transactionIdGenerator;
    private PaymentProcessor paymentProcessor;

    @BeforeEach
    void setUp() {
        transactionIdGenerator = new UuidTransactionIdGenerator();

        List<PaymentMethodValidator> paymentMethodValidators = List.of(
                new VirtualAccountAmountValidator(),
                new VirtualAccountBankCodeValidator(),
                new VirtualAccountHolderNameValidator(),
                new VirtualAccountNumberValidator()
        );

        PaymentMethodValidatorFactory paymentMethodValidatorFactory = new PaymentMethodValidatorFactory(paymentMethodValidators);

        List<PaymentMethod> paymentMethods = List.of(new VirtualAccountPaymentMethod(paymentMethodValidatorFactory));

        PaymentMethodFactory paymentMethodFactory = new PaymentMethodFactory(paymentMethods);

        paymentProcessor = new DefaultPaymentProcessor(paymentMethodFactory);
    }

    @DisplayName("[가상계좌] 가상계좌 수단의 올바른 결제 요청에 대해 결제 성공을 반환한다.")
    @Test
    void testPaymentProcess_withValidRequest_shouldReturnApprovedResult() {
        // given
        String transactionId = transactionIdGenerator.generate();
        PaymentContext paymentContext = new PaymentContext(PaymentMethodType.VIRTUAL_ACCOUNT,
                new VirtualAccountPaymentRequest(
                        10000L,
                        "001",
                        "1234-56789",
                        "홍길동"
                )
        );

        // when
        PaymentResult result = paymentProcessor.process(paymentContext, transactionId);

        // then
        assertThat(result).isNotNull();
        assertThat(result.transactionId()).isEqualTo(transactionId);
        assertThat(result.status()).isEqualTo(PaymentStatus.APPROVED);
        assertThat(result.approvedAt()).isNotNull();
    }

    @DisplayName("[가상계좌] 가상계좌 수단의 결제 요청에 대해 결제 실패를 반환한다.")
    @Test
    void testPaymentProcess_withInvalidRequest_shouldReturnDeclinedResult() {
        // given
        String transactionId = transactionIdGenerator.generate();
        PaymentContext paymentContext = new PaymentContext(PaymentMethodType.VIRTUAL_ACCOUNT,
                new VirtualAccountPaymentRequest(0L, "", "", "")
        );

        // when
        PaymentResult result = paymentProcessor.process(paymentContext, transactionId);

        // then
        assertThat(result).isNotNull();
        assertThat(result.transactionId()).isEqualTo(transactionId);
        assertThat(result.status()).isEqualTo(PaymentStatus.DECLINED);
    }

    @DisplayName("[가상계좌] 잘못된 은행 코드로 결제 요청에 대해 결제 실패를 반환한다.")
    @Test
    void testPaymentProcess_withInvalidBankCode_shouldReturnDeclinedResult() {
        // given
        String transactionId = transactionIdGenerator.generate();
        PaymentContext paymentContext = new PaymentContext(PaymentMethodType.VIRTUAL_ACCOUNT,
                new VirtualAccountPaymentRequest(
                        10000L,
                        "invalid_bank_code",
                        "1234-56789",
                        "홍길동"
                )
        );

        // when
        PaymentResult result = paymentProcessor.process(paymentContext, transactionId);

        // then
        assertThat(result).isNotNull();
        assertThat(result.transactionId()).isEqualTo(transactionId);
        assertThat(result.status()).isEqualTo(PaymentStatus.DECLINED);
        assertThat(result.approvedAt()).isNull();
        assertThat(result.errorCode()).isEqualTo(ErrorType.INVALID_PAYMENT_BANK_CODE.getCode().name());
        assertThat(result.errorMessage()).isEqualTo(ErrorType.INVALID_PAYMENT_BANK_CODE.getMessage());
    }

    @DisplayName("[가상계좌] 가상계좌 번호가 10자리가 아닌 경우 결제 실패를 반환한다.")
    @Test
    void testPaymentProcess_withInvalidVirtualAccountNumber_shouldReturnDeclinedResult() {
        // given
        String transactionId = transactionIdGenerator.generate();
        PaymentContext paymentContext = new PaymentContext(PaymentMethodType.VIRTUAL_ACCOUNT,
                new VirtualAccountPaymentRequest(
                        10000L,
                        "001",
                        "???",
                        "홍길동"
                )
        );

        // when
        PaymentResult result = paymentProcessor.process(paymentContext, transactionId);

        // then
        assertThat(result).isNotNull();
        assertThat(result.transactionId()).isEqualTo(transactionId);
        assertThat(result.status()).isEqualTo(PaymentStatus.DECLINED);
        assertThat(result.approvedAt()).isNull();
        assertThat(result.errorCode()).isEqualTo(ErrorType.INVALID_PAYMENT_VIRTUAL_ACCOUNT_NUMBER.getCode().name());
        assertThat(result.errorMessage()).isEqualTo(ErrorType.INVALID_PAYMENT_VIRTUAL_ACCOUNT_NUMBER.getMessage());
    }

    @DisplayName("[가상계좌] 가상계좌 예금주명이 없는 경우 결제 실패를 반환한다.")
    @Test
    void testPaymentProcess_withInvalidVirtualAccountHolderName_shouldReturnDeclinedResult() {
        // given
        String transactionId = transactionIdGenerator.generate();
        PaymentContext paymentContext = new PaymentContext(PaymentMethodType.VIRTUAL_ACCOUNT,
                new VirtualAccountPaymentRequest(
                        10000L,
                        "001",
                        "1234-56789",
                        ""
                )
        );

        // when
        PaymentResult result = paymentProcessor.process(paymentContext, transactionId);

        // then
        assertThat(result).isNotNull();
        assertThat(result.transactionId()).isEqualTo(transactionId);
        assertThat(result.status()).isEqualTo(PaymentStatus.DECLINED);
        assertThat(result.approvedAt()).isNull();
        assertThat(result.errorCode()).isEqualTo(ErrorType.INVALID_PAYMENT_VIRTUAL_ACCOUNT_HOLDER_NAME.getCode().name());
        assertThat(result.errorMessage()).isEqualTo(ErrorType.INVALID_PAYMENT_VIRTUAL_ACCOUNT_HOLDER_NAME.getMessage());
    }

    @DisplayName("[가상계좌] 결제 요청의 결제 금액이 0원 미만인 경우 결제 실패를 반환한다.")
    @Test
    void testPaymentProcess_withInvalidAmount_shouldReturnDeclinedResult() {
        // given
        String transactionId = transactionIdGenerator.generate();
        PaymentContext paymentContext = new PaymentContext(PaymentMethodType.VIRTUAL_ACCOUNT,
                new VirtualAccountPaymentRequest(-10000L, "001", "1234-56789", "홍길동")
        );

        // when
        PaymentResult result = paymentProcessor.process(paymentContext, transactionId);

        // then
        assertThat(result).isNotNull();
        assertThat(result.transactionId()).isEqualTo(transactionId);
        assertThat(result.status()).isEqualTo(PaymentStatus.DECLINED);
        assertThat(result.approvedAt()).isNull();
        assertThat(result.errorCode()).isEqualTo(ErrorType.INVALID_PAYMENT_AMOUNT.getCode().name());
        assertThat(result.errorMessage()).isEqualTo(ErrorType.INVALID_PAYMENT_AMOUNT.getMessage());
    }

    @DisplayName("[가상계좌] 경계값 테스트: 결제 요청의 결제 금액이 0원인 경우 결제 실패를 반환한다.")
    @Test
    void testPaymentProcess_withZeroAmount_shouldReturnApprovedResult() {
        // given
        String transactionId = transactionIdGenerator.generate();
        PaymentContext paymentContext = new PaymentContext(PaymentMethodType.VIRTUAL_ACCOUNT,
                new VirtualAccountPaymentRequest(0L, "001", "1234-56789", "홍길동")
        );

        // when
        PaymentResult result = paymentProcessor.process(paymentContext, transactionId);

        // then
        assertThat(result).isNotNull();
        assertThat(result.transactionId()).isEqualTo(transactionId);
        assertThat(result.status()).isEqualTo(PaymentStatus.DECLINED);
        assertThat(result.approvedAt()).isNull();
        assertThat(result.errorCode()).isEqualTo(ErrorType.INVALID_PAYMENT_AMOUNT.getCode().name());
        assertThat(result.errorMessage()).isEqualTo(ErrorType.INVALID_PAYMENT_AMOUNT.getMessage());
    }

}
