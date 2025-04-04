package io.hello.demo.cachemodule.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

@Configuration
@EnableCaching
public class CacheConfig {

    @Bean
    public CacheManager cacheManager() {
        SimpleCacheManager cacheManager = new SimpleCacheManager();
        // 가맹점 정보 캐시 설정 (30분 TTL)
        CaffeineCache merchantCache = new CaffeineCache("merchantInfo",
                Caffeine.newBuilder()
                        .expireAfterWrite(10, TimeUnit.SECONDS)
                        .maximumSize(10000)
                        .build());

        // 수수료 계산 결과 캐시 설정 (1시간 TTL)
        CaffeineCache feeRateCache = new CaffeineCache("feeRate",
                Caffeine.newBuilder()
                        .expireAfterWrite(30, TimeUnit.SECONDS)
                        .maximumSize(10000)
                        .build());

        cacheManager.setCaches(Arrays.asList(merchantCache, feeRateCache));
        return cacheManager;
    }

}
