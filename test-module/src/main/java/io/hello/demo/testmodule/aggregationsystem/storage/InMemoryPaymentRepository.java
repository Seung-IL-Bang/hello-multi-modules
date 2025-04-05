package io.hello.demo.testmodule.aggregationsystem.storage;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

public class InMemoryPaymentRepository implements PaymentRepository {

    private final List<Payment> payments = new CopyOnWriteArrayList<>();

    @Override
    public List<Payment> findByMerchantIdAndCreatedAtBetween(String merchantId, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        return payments.stream()
                .filter(p -> p.getMerchantId().equals(merchantId))
                .filter(p -> !p.getCreatedAt().isBefore(startDateTime) && p.getCreatedAt().isBefore(endDateTime))
                .collect(Collectors.toList());
    }

    @Override
    public List<Payment> findByCreatedAtBetween(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        return payments.stream()
                .filter(p -> !p.getCreatedAt().isBefore(startDateTime) && p.getCreatedAt().isBefore(endDateTime))
                .collect(Collectors.toList());
    }

    // 테스트용 메소드
    public void save(Payment payment) {
        payments.add(payment);
    }

    public void clearAll() {
        payments.clear();
    }
}
