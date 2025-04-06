package io.hello.demo.testmodule.unittest.simplesystem.discountinfo;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class DiscountService {
    private final Map<String, DiscountInfo> discountCodeMap;
    private final Map<String, Boolean> usedDiscountCodes;

    public DiscountService() {
        discountCodeMap = new HashMap<>();
        usedDiscountCodes = new HashMap<>();
        // 예시 할인 코드 데이터
        discountCodeMap.put("SUMMER20", new DiscountInfo(0.20, LocalDate.of(2025, 6, 30), false));
        discountCodeMap.put("WELCOME10", new DiscountInfo(0.10, LocalDate.of(2026, 12, 31), false));
        discountCodeMap.put("EXPIRED05", new DiscountInfo(0.05, LocalDate.of(2024, 12, 31), false));
        discountCodeMap.put("USEDCODE", new DiscountInfo(0.15, LocalDate.of(2025, 5, 31), true));
    }

    public double applyDiscount(String discountCode, double orderAmount) {
        DiscountInfo discountInfo = discountCodeMap.get(discountCode);

        if (discountInfo == null) {
            throw new IllegalArgumentException("Invalid discount code: " + discountCode);
        }

        if (discountInfo.isUsed() || usedDiscountCodes.getOrDefault(discountCode, false)) {
            throw new IllegalStateException("This discount code has already been used: " + discountCode);
        }

        if (discountInfo.getExpiryDate().isBefore(LocalDate.now())) {
            throw new IllegalStateException("This discount code has expired: " + discountCode);
        }

        double discountRate = discountInfo.getDiscountRate();
        if (discountRate < 0 || discountRate > 1) {
            throw new IllegalArgumentException("Invalid discount rate: " + discountRate);
        }

        usedDiscountCodes.put(discountCode, true); // 할인 코드 사용 처리
        return orderAmount * (1 - discountRate);
    }

    public void addDiscountCode(String code, DiscountInfo info) {
        this.discountCodeMap.put(code, info);
    }

    public static class DiscountInfo {
        private double discountRate;
        private LocalDate expiryDate;
        private boolean isUsed;

        public DiscountInfo(double discountRate, LocalDate expiryDate, boolean isUsed) {
            this.discountRate = discountRate;
            this.expiryDate = expiryDate;
            this.isUsed = isUsed;
        }

        public double getDiscountRate() {
            return discountRate;
        }

        public LocalDate getExpiryDate() {
            return expiryDate;
        }

        public boolean isUsed() {
            return isUsed;
        }
    }
}
