package io.hello.demo.testmodule.unittest.settlementsystem;

import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface SettlementRepository {
    boolean existsByMerchantIdAndSettlementDate(String merchantId, LocalDate settlementDate);

    Settlement save(Settlement settlement);

    Optional<Settlement> findById(String settlementId);

    List<Settlement> findByMerchantIdAndSettlementDateBetween(String merchantId, LocalDate startDate, LocalDate endDate);
}
