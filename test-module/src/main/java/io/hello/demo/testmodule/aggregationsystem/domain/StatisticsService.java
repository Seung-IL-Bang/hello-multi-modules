package io.hello.demo.testmodule.aggregationsystem.domain;

import io.hello.demo.testmodule.aggregationsystem.api.v1.response.StatisticsResponseDto;
import io.hello.demo.testmodule.aggregationsystem.storage.Payment;
import io.hello.demo.testmodule.aggregationsystem.storage.PaymentRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class StatisticsService {

    private final PaymentRepository paymentRepository;
    private final StatisticsCalculatorFactory calculatorFactory;

    // 캐시 관련 필드
    private final ConcurrentHashMap<String, CacheEntry> statisticsCache = new ConcurrentHashMap<>();
    private final ReadWriteLock cacheLock = new ReentrantReadWriteLock();
    private static final long CACHE_EXPIRY_MILLIS = 10 * 60 * 1000; // 10분


    public StatisticsService(PaymentRepository paymentRepository, StatisticsCalculatorFactory calculatorFactory) {
        this.paymentRepository = paymentRepository;
        this.calculatorFactory = calculatorFactory;
    }

    public StatisticsResponseDto getStatistics(StatisticsRequest request) {
        // 요청 유효성 검증
        validateRequest(request);

        // 캐시 키 생성
        String cacheKey = generateCacheKey(request);

        // 캐시에서 조회 시도
        CacheEntry cachedEntry = getCachedStatistics(cacheKey);
        if (cachedEntry != null && !isCacheExpired(cachedEntry)) {
            return cachedEntry.getResponse();
        }

        // 캐시 미스 또는 만료된 경우 새로 계산
        List<Payment> payments = fetchPayments(request);
        StatisticsCalculator calculator = calculatorFactory.getCalculator(request.getStatisticType());
        StatisticsResponseDto response = calculator.calculate(payments, request);

        // 결과 캐싱
        cacheStatistics(cacheKey, response);

        return response;
    }

    // 통계 계산기 추가 등록 메소드
    public void registerCalculator(StatisticsCalculator calculator) {
        calculatorFactory.registerCalculator(calculator);
    }

    private void validateRequest(StatisticsRequest request) {
        if (request.getStatisticType() == null) {
            throw new IllegalArgumentException("Statistic type is required");
        }

        if (request.getPeriod() == null) {
            throw new IllegalArgumentException("Period is required");
        }

        if (request.getStartDate() == null) {
            throw new IllegalArgumentException("Start date is required");
        }

        if (request.getEndDate() == null) {
            throw new IllegalArgumentException("End date is required");
        }

        if (request.getStartDate().isAfter(request.getEndDate())) {
            throw new IllegalArgumentException("Start date must be before or equal to end date");
        }
    }

    private List<Payment> fetchPayments(StatisticsRequest request) {
        LocalDateTime startDateTime = request.getStartDate().atStartOfDay();
        LocalDateTime endDateTime = request.getEndDate().plusDays(1).atStartOfDay();

        if (request.getMerchantId() != null && !request.getMerchantId().isBlank()) {
            return paymentRepository.findByMerchantIdAndCreatedAtBetween(
                    request.getMerchantId(), startDateTime, endDateTime);
        } else {
            return paymentRepository.findByCreatedAtBetween(startDateTime, endDateTime);
        }
    }

    private String generateCacheKey(StatisticsRequest request) {
        StringBuilder sb = new StringBuilder();
        sb.append(request.getStatisticType()).append("_")
                .append(request.getPeriod()).append("_")
                .append(request.getStartDate()).append("_")
                .append(request.getEndDate()).append("_");

        if (request.getMerchantId() != null) {
            sb.append(request.getMerchantId()).append("_");
        }

        if (request.getGroupBy() != null && !request.getGroupBy().isEmpty()) {
            request.getGroupBy()
                    .stream()
                    .map(StatisticsGroupType::name)
                    .forEach(group -> sb.append(group).append(","));
        }

        return sb.toString();
    }

    private CacheEntry getCachedStatistics(String cacheKey) {
        cacheLock.readLock().lock();
        try {
            return statisticsCache.get(cacheKey);
        } finally {
            cacheLock.readLock().unlock();
        }
    }

    private boolean isCacheExpired(CacheEntry entry) {
        return System.currentTimeMillis() - entry.getCreatedAt() > CACHE_EXPIRY_MILLIS;
    }

    private void cacheStatistics(String cacheKey, StatisticsResponseDto response) {
        cacheLock.writeLock().lock();
        try {
            statisticsCache.put(cacheKey, new CacheEntry(response, System.currentTimeMillis()));
        } finally {
            cacheLock.writeLock().unlock();
        }
    }

    // 캐시 엔트리 클래스
    private static class CacheEntry {
        private final StatisticsResponseDto response;
        private final long createdAt;

        public CacheEntry(StatisticsResponseDto response, long createdAt) {
            this.response = response;
            this.createdAt = createdAt;
        }

        public StatisticsResponseDto getResponse() {
            return response;
        }

        public long getCreatedAt() {
            return createdAt;
        }
    }


}
