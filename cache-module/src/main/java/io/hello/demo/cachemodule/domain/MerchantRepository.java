package io.hello.demo.cachemodule.domain;

import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class MerchantRepository {

    private static final Map<String, Merchant> merchantMap = new ConcurrentHashMap<>(
            Map.of(
                    "merchant-1", new Merchant("1234567890", "가맹점A", "123-45-67890"),
                    "merchant-2", new Merchant("0987654321", "가맹점B", "987-65-43210")
            )
    );

    // 가맹점 정보를 DB에서 조회하는 메서드
    public Merchant findMerchantById(String merchantId) {
        // DB에서 가맹점 정보를 조회하는 로직
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Thread was interrupted", e);
        }
        return merchantMap.getOrDefault(merchantId, null);
    }

    // 가맹점 정보를 DB에 저장하는 메서드
    public void save(Merchant merchant) {
        // DB에 가맹점 정보를 저장하는 로직
        // 예시로 ConcurrentHashMap에 저장
        merchantMap.put(merchant.getId(), merchant);
    }

}
