package io.hello.demo.faulttolerancemodule.ratelimiter;

import org.springframework.stereotype.Service;

@Service
public class SimpleService {
    
    private final RateLimiterService rateLimiterService;
    
    public SimpleService(RateLimiterService rateLimiterService) {
        this.rateLimiterService = rateLimiterService;
    }

    public String process(String key) {
        // 기본 RateLimitConfig를 사용하여 요청을 처리합니다.
        return process(key, RateLimitConfig.ofDefault());
    }
    
    public String process(String key, RateLimitConfig config) {
        // RateLimiterService를 사용하여 요청을 처리합니다.
        boolean isAcquired = rateLimiterService.tryAcquire(key, config);

        if (!isAcquired) {
            return "Rate limit exceeded for key: " + key;
        }

        return "Processed request with key: " + key;
    }
}
