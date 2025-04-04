package io.hello.demo.cachemodule.domain;

import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class MerchantService {

    private final MerchantRepository merchantRepository;
    private final FeeRateCalculator feeRateCalculator;

    public MerchantService(MerchantRepository merchantRepository, FeeRateCalculator feeRateCalculator) {
        this.merchantRepository = merchantRepository;
        this.feeRateCalculator = feeRateCalculator;
    }

    // 가맹점 정보를 조회하는 메서드
    @Cacheable(value = "merchantInfo", key = "#merchantId", unless = "#result == null")
    public MerchantInfo getMerchantInfo(String merchantId) {
        // DB에서 가맹점 정보를 조회하는 로직
        Merchant merchant = merchantRepository.findMerchantById(merchantId);

        if (merchant == null) {
            return null; // 가맹점 정보가 없는 경우
        }

        BigDecimal feeRate = feeRateCalculator.calculateFeeRate(merchant);

        return new MerchantInfo(
                merchant.getId(),
                merchant.getName(),
                merchant.getBusinessNumber(),
                feeRate.doubleValue()
        );
    }


    // 가맹점 정보 업데이트 시 캐시 갱신
    @CachePut(value = "merchantInfo", key = "#result.id")
    public MerchantInfo updateMerchantInfo(Merchant merchant) {
        merchantRepository.save(merchant);

        // feeRate 캐시도 함께 제거
        feeRateCalculator.evictFeeRateCache(merchant.getId());

        BigDecimal reCalculateFeeRate = feeRateCalculator.calculateFeeRate(merchant);

        return new MerchantInfo(
                merchant.getId(),
                merchant.getName(),
                merchant.getBusinessNumber(),
                reCalculateFeeRate.doubleValue()
        );
    }
}
