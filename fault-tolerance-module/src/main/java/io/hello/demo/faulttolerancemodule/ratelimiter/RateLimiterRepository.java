package io.hello.demo.faulttolerancemodule.ratelimiter;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface RateLimiterRepository extends JpaRepository<RateLimit, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT r FROM RateLimit r WHERE r.rateLimitKey = :key")
    Optional<RateLimit> findByKeyWithLock(@Param("key") String key);

}
