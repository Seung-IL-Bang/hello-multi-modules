package io.hello.demo.testmodule.firstcome.api.v1.response;

import io.hello.demo.testmodule.firstcome.domain.EventParticipationResult;

import java.time.LocalDateTime;

public class EventParticipationResponseDto {
    private String result;
    private String couponId;
    private String message;
    private Integer discountAmount;
    private LocalDateTime expiryDate;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getCouponId() {
        return couponId;
    }

    public void setCouponId(String couponId) {
        this.couponId = couponId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(Integer discountAmount) {
        this.discountAmount = discountAmount;
    }

    public LocalDateTime getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDateTime expiryDate) {
        this.expiryDate = expiryDate;
    }

    public static EventParticipationResponseDto of(EventParticipationResult result) {
        EventParticipationResponseDto response = new EventParticipationResponseDto();
        response.result = result.getResult();
        response.couponId = result.getCouponId();
        response.message = result.getMessage();
        response.discountAmount = result.getDiscountAmount();
        response.expiryDate = result.getExpiryDate();
        return response;
    }
}
