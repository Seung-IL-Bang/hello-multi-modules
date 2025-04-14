package io.hello.demo.faulttolerancemodule.ratelimiter;

import io.github.resilience4j.ratelimiter.RateLimiterConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;

@Service
public class RateLimiterService {

    private final Logger log = LoggerFactory.getLogger(RateLimiterService.class);
    private final RateLimiterRepository rateLimiterRepository;
    private final RateLimitConfig rateLimitDefaultConfig;

    public RateLimiterService(RateLimiterRepository rateLimiterRepository) {
        this.rateLimiterRepository = rateLimiterRepository;
        this.rateLimitDefaultConfig = RateLimitConfig.ofDefault();
    }

    @Transactional(timeout = 5)
    public boolean tryAcquire(String key) {
        return tryAcquire(key, rateLimitDefaultConfig);
    }

    @Transactional(timeout = 5)
    public boolean tryAcquire(String key, RateLimitConfig config) {
        try {
            RateLimit rateLimit = rateLimiterRepository
                    .findByKeyWithLock(key)
                    .orElseGet(() -> createRateLimit(key, config.getCapacity()));

            // 토큰 리필 계산
            refillTokens(rateLimit, config);

            // 토큰 사용 가능 여부 확인
            if (rateLimit.getTokenCount() > 0) {
                rateLimit.decrementTokenCount();
                log.info("token count: {}, last refill time: {}", rateLimit.getTokenCount(), rateLimit.getLastRefillTime());
                return true;
            } else {
                log.debug("Rate limit exceeded for key: {}", key);
                return false;
            }
        } catch (Exception e) {
            log.error("Error during rate limiting for key: {}", key, e);
            // 장애 상황에서는 기본적으로 요청 허용 (fail-open 정책)
            return true;
        }
    }

    private RateLimit createRateLimit(String key, long capacity) {
        RateLimit rateLimit = new RateLimit(key, capacity);
        return rateLimiterRepository.save(rateLimit);
    }

    private void refillTokens(RateLimit rateLimit, RateLimitConfig config) {
        LocalDateTime now = LocalDateTime.now();
        Duration sinceLastRefill = Duration.between(rateLimit.getLastRefillTime(), now);

        long refillPeriodMillis = config.getRefillPeriod().toMillis();

        if (sinceLastRefill.toMillis() >= refillPeriodMillis) {
            long periodsElapsed = sinceLastRefill.toMillis() / refillPeriodMillis;
            long tokensToAdd = periodsElapsed * config.getTokensPerPeriod();

            // 최대 용량 제한
            long newTokenCount = Math.min(rateLimit.getTokenCount() + tokensToAdd, config.getCapacity());

            // 마지막 리필 시간 업데이트 (정확한 주기 유지)
            LocalDateTime newRefillTime = rateLimit.getLastRefillTime()
                    .plusNanos(periodsElapsed * refillPeriodMillis * 1_000_000);

            rateLimit.refillTokens(newTokenCount, newRefillTime);
        }
    }
}
