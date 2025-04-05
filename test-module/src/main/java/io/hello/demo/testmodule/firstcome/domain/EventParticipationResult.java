package io.hello.demo.testmodule.firstcome.domain;

import java.time.LocalDateTime;

public class EventParticipationResult {

    private String result;
    private String couponId;
    private String message;
    private Integer discountAmount;
    private LocalDateTime expiryDate;

    public EventParticipationResult() {
    }

    // 성공 응답 생성 팩토리 메소드
    public static EventParticipationResult success(String couponId, int discountAmount, LocalDateTime expiryDate) {
        EventParticipationResult response = new EventParticipationResult();
        response.result = "SUCCESS";
        response.couponId = couponId;
        response.message = "축하합니다! 할인 쿠폰이 발급되었습니다.";
        response.discountAmount = discountAmount;
        response.expiryDate = expiryDate;
        return response;
    }

    // 실패 응답 생성 팩토리 메소드
    public static EventParticipationResult failure(String reason) {
        EventParticipationResult response = new EventParticipationResult();
        response.result = "FAILURE";
        response.message = reason;
        return response;
    }

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
}
