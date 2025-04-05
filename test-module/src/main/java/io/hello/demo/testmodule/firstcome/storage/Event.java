package io.hello.demo.testmodule.firstcome.storage;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;

import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicInteger;

@Entity
public class Event {

    @Id
    private String eventId;
    private String name;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private int totalCouponCount;
    private AtomicInteger remainingCouponCount;
    @Enumerated(EnumType.STRING)
    private EventStatus status;

    public Event() {
    }

    public Event(String eventId, String name, LocalDateTime startTime,
                 LocalDateTime endTime, int totalCouponCount) {
        this.eventId = eventId;
        this.name = name;
        this.startTime = startTime;
        this.endTime = endTime;
        this.totalCouponCount = totalCouponCount;
        this.remainingCouponCount = new AtomicInteger(totalCouponCount);

        // 초기 상태 설정
        LocalDateTime now = LocalDateTime.now();
        if (now.isBefore(startTime)) {
            this.status = EventStatus.PENDING;
        } else if (now.isAfter(endTime)) {
            this.status = EventStatus.ENDED;
        } else {
            this.status = EventStatus.ACTIVE;
        }
    }

    // 쿠폰 차감 시도 (원자적 연산)
    public boolean tryDecrementCoupon() {
        // 남은 쿠폰이 있는 경우에만 차감하고 true 반환
        int current;
        do {
            current = remainingCouponCount.get();
            if (current <= 0) {
                return false;
            }
        } while (!remainingCouponCount.compareAndSet(current, current - 1));

        return true;
    }

    // 이벤트 상태 업데이트
    public void updateStatus() {
        LocalDateTime now = LocalDateTime.now();
        if (now.isBefore(startTime)) {
            this.status = EventStatus.PENDING;
        } else if (now.isAfter(endTime) || remainingCouponCount.get() <= 0) {
            this.status = EventStatus.ENDED;
        } else {
            this.status = EventStatus.ACTIVE;
        }
    }

    public String getEventId() {
        return eventId;
    }

    public String getName() {
        return name;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public int getTotalCouponCount() {
        return totalCouponCount;
    }

    public AtomicInteger getRemainingCouponCount() {
        return remainingCouponCount;
    }

    public EventStatus getStatus() {
        return status;
    }

    public static class Builder {
        private String eventId;
        private String name;
        private LocalDateTime startTime;
        private LocalDateTime endTime;
        private int totalCouponCount;
        private AtomicInteger remainingCouponCount;
        private EventStatus status;

        public Builder eventId(String eventId) {
            this.eventId = eventId;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder startTime(LocalDateTime startTime) {
            this.startTime = startTime;
            return this;
        }

        public Builder endTime(LocalDateTime endTime) {
            this.endTime = endTime;
            return this;
        }

        public Builder totalCouponCount(int totalCouponCount) {
            this.totalCouponCount = totalCouponCount;
            return this;
        }

        public Builder remainingCouponCount(AtomicInteger remainingCouponCount) {
            this.remainingCouponCount = remainingCouponCount;
            return this;
        }

        public Builder status(EventStatus status) {
            this.status = status;
            return this;
        }

        public Event build() {
            Event event = new Event();
            event.eventId = this.eventId;
            event.name = this.name;
            event.startTime = this.startTime;
            event.endTime = this.endTime;
            event.totalCouponCount = this.totalCouponCount;
            event.remainingCouponCount = this.remainingCouponCount;
            event.status = this.status;

            return event;
        }
    }
}
