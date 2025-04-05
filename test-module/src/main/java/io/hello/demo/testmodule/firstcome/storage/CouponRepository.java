package io.hello.demo.testmodule.firstcome.storage;


import java.util.List;
import java.util.Optional;

public interface CouponRepository {
    Coupon save(Coupon couponData);
    Optional<Coupon> findById(String couponId);
    List<Coupon> findByUserId(String userId);
    List<Coupon> findByEventId(String eventId);
    boolean existsByEventIdAndUserId(String eventId, String userId);
}
