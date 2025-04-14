package io.hello.demo.faulttolerancemodule.ratelimiter;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "rate_limit")
public class RateLimit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "rate_limit_key", nullable = false, unique = true)
    private String rateLimitKey;

    @Column(name = "token_count", nullable = false)
    private Long tokenCount;

    @Column(name = "last_refill_time", nullable = false)
    private LocalDateTime lastRefillTime;

    public RateLimit() {
    }

    public RateLimit(String rateLimitKey, Long tokenCount) {
        this.rateLimitKey = rateLimitKey;
        this.tokenCount = tokenCount;
        this.lastRefillTime = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public String getRateLimitKey() {
        return rateLimitKey;
    }

    public Long getTokenCount() {
        return tokenCount;
    }

    public LocalDateTime getLastRefillTime() {
        return lastRefillTime;
    }

    public void decrementTokenCount() {
        this.tokenCount--;
    }

    public void refillTokens(long newTokens, LocalDateTime newRefillTime) {
        this.tokenCount = newTokens;
        this.lastRefillTime = newRefillTime;
    }
}
