package io.hello.demo.testmodule.unittest.settlementsystem;

import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TransactionRepository {
    List<Transaction> findByMerchantIdAndCreatedAtBetween(String merchantId, LocalDateTime startDateTime, LocalDateTime endDateTime);
}
