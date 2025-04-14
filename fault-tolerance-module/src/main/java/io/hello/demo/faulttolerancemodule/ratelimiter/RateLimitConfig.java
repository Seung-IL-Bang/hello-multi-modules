package io.hello.demo.faulttolerancemodule.ratelimiter;

import java.time.Duration;

public class RateLimitConfig {

    // 토큰 버킷의 최대 용량
    private final long capacity;
    // 리필 주기마다 추가할 토큰 수
    private final long tokensPerPeriod;
    // 리필 주기
    private final Duration refillPeriod;

    private RateLimitConfig(long capacity, long tokensPerPeriod, Duration refillPeriod) {
        this.capacity = capacity;
        this.tokensPerPeriod = tokensPerPeriod;
        this.refillPeriod = refillPeriod;
    }

    public long getCapacity() {
        return capacity;
    }

    public long getTokensPerPeriod() {
        return tokensPerPeriod;
    }

    public Duration getRefillPeriod() {
        return refillPeriod;
    }

    // ----------- Static Factory Methods -----------

    public static RateLimitConfig ofDefault() {
        return RateLimitConfig.builder()
                .capacity(3)
                .tokensPerPeriod(1)
                .refillPeriod(Duration.ofSeconds(5))
                .build();
    }

    public static RateLimitConfig perSecond(long requestsPerSecond) {
        return RateLimitConfig.builder()
                .capacity(requestsPerSecond)
                .tokensPerPeriod(requestsPerSecond)
                .refillPeriod(Duration.ofSeconds(1))
                .build();
    }

    public static RateLimitConfig perHour(long requestsPerHour) {
        return RateLimitConfig.builder()
                .capacity(requestsPerHour)
                .tokensPerPeriod(requestsPerHour)
                .refillPeriod(Duration.ofHours(1))
                .build();
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private long capacity;
        private long tokensPerPeriod;
        private Duration refillPeriod;

        public Builder capacity(long capacity) {
            this.capacity = capacity;
            return this;
        }

        public Builder tokensPerPeriod(long tokensPerPeriod) {
            this.tokensPerPeriod = tokensPerPeriod;
            return this;
        }

        public Builder refillPeriod(Duration refillPeriod) {
            this.refillPeriod = refillPeriod;
            return this;
        }

        public RateLimitConfig build() {
            return new RateLimitConfig(capacity, tokensPerPeriod, refillPeriod);
        }
    }
}
