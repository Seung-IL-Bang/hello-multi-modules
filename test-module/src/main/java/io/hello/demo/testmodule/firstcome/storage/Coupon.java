package io.hello.demo.testmodule.firstcome.storage;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.time.LocalDateTime;

@Entity
public class Coupon {

    @Id
    private String couponId;
    private String eventId;
    private String userId;
    private int discountAmount;
    private LocalDateTime issuedAt;
    private LocalDateTime expiryDate;

    public String getCouponId() {
        return couponId;
    }

    public String getEventId() {
        return eventId;
    }

    public String getUserId() {
        return userId;
    }

    public int getDiscountAmount() {
        return discountAmount;
    }

    public LocalDateTime getIssuedAt() {
        return issuedAt;
    }

    public LocalDateTime getExpiryDate() {
        return expiryDate;
    }

    public static class Builder {
        private String couponId;
        private String eventId;
        private String userId;
        private int discountAmount;
        private LocalDateTime issuedAt;
        private LocalDateTime expiryDate;

        public Builder couponId(String couponId) {
            this.couponId = couponId;
            return this;
        }

        public Builder eventId(String eventId) {
            this.eventId = eventId;
            return this;
        }

        public Builder userId(String userId) {
            this.userId = userId;
            return this;
        }

        public Builder discountAmount(int discountAmount) {
            this.discountAmount = discountAmount;
            return this;
        }

        public Builder issuedAt(LocalDateTime issuedAt) {
            this.issuedAt = issuedAt;
            return this;
        }

        public Builder expiryDate(LocalDateTime expiryDate) {
            this.expiryDate = expiryDate;
            return this;
        }

        public Coupon build() {
            Coupon coupon = new Coupon();
            coupon.couponId = this.couponId;
            coupon.eventId = this.eventId;
            coupon.userId = this.userId;
            coupon.discountAmount = this.discountAmount;
            coupon.issuedAt = this.issuedAt;
            coupon.expiryDate = this.expiryDate;
            return coupon;
        }
    }
}
