package io.hello.demo.testmodule.unittest;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final PgClient pgClient;

    public PaymentService(PaymentRepository paymentRepository, PgClient pgClient) {
        this.paymentRepository = paymentRepository;
        this.pgClient = pgClient;
    }

    @Transactional
    public PaymentResponse processPayment(PaymentRequest request) {
        // 요청 유효성 검증
        validatePaymentRequest(request);

        // 결제 생성
        Payment payment = createPayment(request);

        try {
            // PG사 결제 요청
            String pgTraceId = pgClient.requestPayment(
                    payment.getId(),
                    request.getAmount(),
                    request.getCardInfo()
            );

            // 결제 정보 업데이트
            payment.approve(pgTraceId);
            paymentRepository.save(payment);

            return PaymentResponse.success(payment);
        } catch (Exception e) {
            // 결제 실패 처리
            payment.fail(e.getMessage());
            paymentRepository.save(payment);

            throw new InvalidPaymentException("Payment processing failed: " + e.getMessage());
        }
    }

    @Transactional
    public PaymentResponse cancelPayment(String paymentId, BigDecimal amount) {
        // 결제 조회
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new PaymentNotFoundException("Payment not found: " + paymentId));

        // 취소 가능 여부 확인
        if (!payment.canCancel()) {
            throw new InvalidPaymentException("Payment cannot be cancelled");
        }

        // 취소 금액 유효성 확인
        if (amount.compareTo(payment.getAmount()) > 0) {
            throw new InvalidPaymentException("Cancel amount cannot exceed payment amount");
        }

        try {
            // PG사 결제 취소 요청
            String pgCancelId = pgClient.cancelPayment(
                    payment.getId(),
                    payment.getPgTraceId(),
                    amount
            );

            // 결제 정보 업데이트
            payment.cancel(pgCancelId, amount);
            paymentRepository.save(payment);

            return PaymentResponse.success(payment);
        } catch (Exception e) {
            throw new InvalidPaymentException("Payment cancellation failed: " + e.getMessage());
        }
    }

    private void validatePaymentRequest(PaymentRequest request) {
        if (request.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidPaymentException("Payment amount must be positive");
        }

        if (request.getCardInfo() == null) {
            throw new InvalidPaymentException("Card information is required");
        }

        // 추가 유효성 검증 로직...
    }

    private Payment createPayment(PaymentRequest request) {
        Payment payment = new Payment();
        payment.setId(generatePaymentId());
        payment.setAmount(request.getAmount());
        payment.setStatus(PaymentStatus.CREATED);
        payment.setCustomerId(request.getCustomerId());
        payment.setCreatedAt(LocalDateTime.now());

        return paymentRepository.save(payment);
    }

    private String generatePaymentId() {
        return "PAYMENT-" + UUID.randomUUID().toString().substring(0, 8);
    }

}
