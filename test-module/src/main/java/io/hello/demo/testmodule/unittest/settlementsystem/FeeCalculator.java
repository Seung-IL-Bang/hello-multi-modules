package io.hello.demo.testmodule.unittest.settlementsystem;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Component
public class FeeCalculator {

    public BigDecimal calculateFee(Merchant merchant, BigDecimal amount) {
        // 가맹점별 수수료율 적용
        BigDecimal feeRate = merchant.getFeeRate();
        return amount.multiply(feeRate).setScale(0, RoundingMode.HALF_UP);
    }
}
