package io.hello.demo.testmodule.aggregationsystem.domain;

import io.hello.demo.testmodule.aggregationsystem.storage.Payment;
import io.hello.demo.testmodule.aggregationsystem.storage.PaymentMethod;
import io.hello.demo.testmodule.aggregationsystem.storage.PaymentStatus;
import io.hello.demo.testmodule.aggregationsystem.support.DateUtil;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PaymentMethodRatioCalculator implements StatisticsCalculator {
    @Override
    public StatisticsType getStatisticType() {
        return StatisticsType.PAYMENT_METHOD_RATIO;
    }

    @Override
    public StatisticsResult calculate(List<Payment> payments, StatisticsRequest request) {
        // 결제 상태가 승인된 것만 필터링
        List<Payment> approvedPayments = payments.stream()
                .filter(p -> PaymentStatus.APPROVED.equals(p.getStatus()))
                .toList();

        // 기간별 데이터 분류
        Map<String, List<Payment>> paymentsByPeriod = new HashMap<>();

        for (Payment payment : approvedPayments) {
            String periodKey = DateUtil.formatDateByPeriod(payment.getCreatedAt(), request.getPeriod());
            paymentsByPeriod.computeIfAbsent(periodKey, k -> new ArrayList<>()).add(payment);
        }

        // 응답 구성
        StatisticsResult response = new StatisticsResult();
        response.setStatisticType(getStatisticType());
        response.setPeriod(request.getPeriod());

        List<PeriodData> periodDataList = new ArrayList<>();

        for (Map.Entry<String, List<Payment>> entry : paymentsByPeriod.entrySet()) {
            String periodKey = entry.getKey();
            List<Payment> periodPayments = entry.getValue();

            PeriodData periodData = new PeriodData();
            periodData.setDate(periodKey);

            // 결제 수단별로 그룹화
            Map<PaymentMethod, List<Payment>> paymentsByMethod = periodPayments.stream()
                    .collect(Collectors.groupingBy(Payment::getPaymentMethod));

            int totalCount = periodPayments.size();

            // 각 결제 수단별 비율 계산
            List<DetailData> details = new ArrayList<>();

            for (Map.Entry<PaymentMethod, List<Payment>> methodEntry : paymentsByMethod.entrySet()) {
                PaymentMethod method = methodEntry.getKey();
                List<Payment> methodPayments = methodEntry.getValue();

                DetailData detail = new DetailData();
                detail.setPaymentMethod(method);
                detail.setCount(methodPayments.size());

                // 비율 계산 (소수점 2자리까지)
                BigDecimal ratio = BigDecimal.valueOf(methodPayments.size())
                        .divide(BigDecimal.valueOf(totalCount), 4, RoundingMode.HALF_UP)
                        .multiply(BigDecimal.valueOf(100))
                        .setScale(2, RoundingMode.HALF_UP);

                // additionalData에 비율 정보 추가
                Map<String, Object> additionalData = new HashMap<>();
                additionalData.put("ratio", ratio);
                detail.setAdditionalData(additionalData);

                details.add(detail);
            }

            periodData.setDetails(details);
            periodDataList.add(periodData);
        }

        response.setData(periodDataList);
        return response;
    }
}
