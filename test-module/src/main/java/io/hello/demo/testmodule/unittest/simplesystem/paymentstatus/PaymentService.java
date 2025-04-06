package io.hello.demo.testmodule.unittest.simplesystem.paymentstatus;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("paymentStatusService")
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final PointService pointService;
    private final RefundService refundService;

    public PaymentService(PaymentRepository paymentRepository, PointService pointService, RefundService refundService) {
        this.paymentRepository = paymentRepository;
        this.pointService = pointService;
        this.refundService = refundService;
    }

    @Transactional
    public void processPayment(Long paymentId, String targetStatus) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new IllegalArgumentException("Payment not found"));

        switch (targetStatus.toUpperCase()) {
            case "SUCCESS":
                payment.setStatus("SUCCESS");
                pointService.addPoints(payment.getUserId(), payment.getAmount() / 10);
                break;
            case "CANCELLED":
                payment.setStatus("CANCELLED");
                refundService.processRefund(payment.getId(), payment.getAmount());
                break;
            default:
                throw new IllegalStateException("Invalid status: " + targetStatus);
        }
        paymentRepository.save(payment);
    }

}
