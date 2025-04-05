package io.hello.demo.testmodule.aggregationsystem.domain;

import java.util.HashMap;
import java.util.Map;

public class StatisticsCalculatorFactory {

    private final Map<StatisticsType, StatisticsCalculator> calculators = new HashMap<>();

    public StatisticsCalculatorFactory() {
        // 기본 계산기 등록
        registerCalculator(new PaymentAmountCalculator());
        registerCalculator(new PaymentCountCalculator());
        registerCalculator(new PaymentMethodRatioCalculator());
    }

    public void registerCalculator(StatisticsCalculator calculator) {
        calculators.put(calculator.getStatisticType(), calculator);
    }

    public StatisticsCalculator getCalculator(StatisticsType statisticType) {
        StatisticsCalculator calculator = calculators.get(statisticType);
        if (calculator == null) {
            throw new IllegalArgumentException("Unsupported statistic type: " + statisticType);
        }
        return calculator;
    }
}
