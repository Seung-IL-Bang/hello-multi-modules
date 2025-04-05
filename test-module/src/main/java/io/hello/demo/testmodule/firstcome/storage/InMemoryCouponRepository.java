package io.hello.demo.testmodule.firstcome.storage;


import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class InMemoryCouponRepository implements CouponRepository {

    private final Map<String, Coupon> couponStore = new ConcurrentHashMap<>();

    @Override
    public Coupon save(Coupon couponData) {
        couponStore.put(couponData.getCouponId(), couponData);
        return couponData;
    }

    @Override
    public Optional<Coupon> findById(String couponId) {
        return Optional.ofNullable(couponStore.getOrDefault(couponId, null));
    }

    @Override
    public List<Coupon> findByUserId(String userId) {
        return couponStore.values().stream()
                .filter(couponData -> couponData.getUserId().equals(userId))
                .collect(Collectors.toList());
    }

    @Override
    public List<Coupon> findByEventId(String eventId) {
        return couponStore.values().stream()
                .filter(couponData -> couponData.getEventId().equals(eventId))
                .collect(Collectors.toList());
    }

    @Override
    public boolean existsByEventIdAndUserId(String eventId, String userId) {
        return couponStore.values().stream()
                .anyMatch(couponData -> couponData.getEventId().equals(eventId) &&
                        couponData.getUserId().equals(userId));
    }
}
