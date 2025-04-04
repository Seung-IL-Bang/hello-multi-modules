package io.hello.demo.cachemodule.domain;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Component
public class FeeRateCalculator {

    @Cacheable(value = "feeRate", key = "#merchant.id", unless = "#result == null")
    public BigDecimal calculateFeeRate(Merchant merchant) {
        // 가맹점 ID에 따라 수수료율을 계산하는 로직
        try {
            Thread.sleep(1000); // 1초 대기 (예시)
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Thread was interrupted", e);
        }
        return BigDecimal.valueOf(Math.random() * 10).setScale(2, RoundingMode.HALF_UP);
    }

    // 캐시 제거 메소드
    @CacheEvict(value = "feeRate", key = "#merchantId")
    public void evictFeeRateCache(String merchantId) {
        // 메소드 내용은 없어도 됨 - 캐시 제거용
    }

}
