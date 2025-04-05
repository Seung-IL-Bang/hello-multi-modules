package io.hello.demo.testmodule.firstcome.domain;

import io.hello.demo.testmodule.firstcome.storage.Coupon;
import io.hello.demo.testmodule.firstcome.storage.CouponRepository;
import io.hello.demo.testmodule.firstcome.storage.Event;
import io.hello.demo.testmodule.firstcome.storage.EventStatus;

import java.time.LocalDateTime;
import java.util.UUID;

public class ActiveEventStrategy implements EventProcessingStrategy {

    private final CouponRepository couponRepository;
    private final int discountAmount;
    private final int expiryDays;

    public ActiveEventStrategy(CouponRepository couponRepository, int discountAmount, int expiryDays) {
        this.couponRepository = couponRepository;
        this.discountAmount = discountAmount;
        this.expiryDays = expiryDays;
    }

    @Override
    public EventParticipationResult process(Event event, EventParticipationRequest request) {
        // 이미 참여한 사용자인지 확인
        if (couponRepository.existsByEventIdAndUserId(event.getEventId(), request.getUserId())) {
            return EventParticipationResult.failure("이미 쿠폰을 발급받으셨습니다.");
        }

        // 쿠폰 차감 시도
        if (!event.tryDecrementCoupon()) {
            // 쿠폰 소진되면 이벤트 상태 업데이트
            event.updateStatus();
            return EventParticipationResult.failure("쿠폰이 모두 소진되었습니다.");
        }

        // 쿠폰 생성 및 저장
        String couponId = "COUPON:" + UUID.randomUUID().toString().substring(0, 4);
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expiryDate = now.plusDays(expiryDays);

        Coupon coupon = new Coupon.Builder()
                .couponId(couponId)
                .eventId(event.getEventId())
                .userId(request.getUserId())
                .discountAmount(discountAmount)
                .issuedAt(now)
                .expiryDate(expiryDate)
                .build();

        couponRepository.save(coupon);

        // 성공 응답 반환
        return EventParticipationResult.success(couponId, discountAmount, expiryDate);
    }

    @Override
    public boolean canProcess(Event event) {
        return event.getStatus() == EventStatus.ACTIVE;
    }
}
