package io.hello.demo.testmodule.aggregationsystem.domain;

import io.hello.demo.testmodule.aggregationsystem.storage.Payment;

import java.util.List;

// 통계 계산기 인터페이스 - For 전략 패턴
public interface StatisticsCalculator {
    StatisticsType getStatisticType();
    StatisticsResult calculate(List<Payment> payments, StatisticsRequest request);
}
