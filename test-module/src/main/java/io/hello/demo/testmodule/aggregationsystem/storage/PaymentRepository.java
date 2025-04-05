package io.hello.demo.testmodule.aggregationsystem.storage;

import java.time.LocalDateTime;
import java.util.List;

// 통계 저장소 인터페이스
public interface PaymentRepository {
    List<Payment> findByMerchantIdAndCreatedAtBetween(String merchantId, LocalDateTime startDateTime, LocalDateTime endDateTime);
    List<Payment> findByCreatedAtBetween(LocalDateTime startDateTime, LocalDateTime endDateTime);
}
