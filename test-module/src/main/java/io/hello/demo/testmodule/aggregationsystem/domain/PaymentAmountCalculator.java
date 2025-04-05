package io.hello.demo.testmodule.aggregationsystem.domain;

import io.hello.demo.testmodule.aggregationsystem.api.v1.response.StatisticsResponseDto;
import io.hello.demo.testmodule.aggregationsystem.storage.Payment;
import io.hello.demo.testmodule.aggregationsystem.storage.PaymentStatus;
import io.hello.demo.testmodule.aggregationsystem.support.DateUtil;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PaymentAmountCalculator implements StatisticsCalculator {
    @Override
    public StatisticsType getStatisticType() {
        return StatisticsType.PAYMENT_AMOUNT;
    }

    @Override
    public StatisticsResponseDto calculate(List<Payment> payments, StatisticsRequest request) {
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
        StatisticsResponseDto response = new StatisticsResponseDto();
        response.setStatisticType(getStatisticType());
        response.setPeriod(request.getPeriod());

        List<PeriodData> periodDataList = new ArrayList<>();

        for (Map.Entry<String, List<Payment>> entry : paymentsByPeriod.entrySet()) {
            String periodKey = entry.getKey();
            List<Payment> periodPayments = entry.getValue();

            PeriodData periodData = new PeriodData();
            periodData.setDate(periodKey);

            // 그룹화 기준으로 데이터 분류
            List<DetailData> details = new ArrayList<>();

            if (request.getGroupBy() != null && !request.getGroupBy().isEmpty()) {
                // 그룹화 기준이 있는 경우
                Map<String, List<Payment>> groupedPayments = groupPaymentsByRequest(periodPayments, request.getGroupBy());

                for (Map.Entry<String, List<Payment>> groupEntry : groupedPayments.entrySet()) {
                    String groupKey = groupEntry.getKey();
                    List<Payment> groupPayments = groupEntry.getValue();

                    DetailData detail = createDetailData(groupKey, groupPayments, request.getGroupBy());
                    details.add(detail);
                }
            } else {
                // 그룹화 기준이 없는 경우 전체 합산
                DetailData detail = new DetailData();
                BigDecimal totalAmount = periodPayments.stream()
                        .map(Payment::getAmount)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);

                detail.setAmount(totalAmount);
                detail.setCount(periodPayments.size());
                details.add(detail);
            }

            periodData.setDetails(details);
            periodDataList.add(periodData);
        }

        response.setData(periodDataList);
        return response;
    }

    // 그룹화 기준에 따라 결제 데이터 분류
    private Map<String, List<Payment>> groupPaymentsByRequest(List<Payment> payments, List<StatisticsGroupType> groupBy) {
        Map<String, List<Payment>> groupedPayments = new HashMap<>();
        for (Payment payment : payments) {
            StringBuilder keyBuilder = new StringBuilder();

            for (StatisticsGroupType groupType : groupBy) {
                String value = groupType.getValueFrom(payment);
                keyBuilder.append(value).append(":");
            }

            String key = keyBuilder.toString();
            if (key.endsWith("_")) {
                key = key.substring(0, key.length() - 1);
            }

            groupedPayments.computeIfAbsent(key, k -> new ArrayList<>()).add(payment);
        }
        return groupedPayments;
    }

    // 그룹별 DetailData 생성
    private DetailData createDetailData(String groupKey, List<Payment> payments, List<StatisticsGroupType> groupBy) {
        DetailData detail = new DetailData();

        // 그룹 키 파싱
        String[] valueParts = groupKey.split(":");

        // 각 enum 타입별로 값 설정
        for (int i = 0; i < Math.min(valueParts.length, groupBy.size()); i++) {
            StatisticsGroupType type = groupBy.get(i);
            String value = valueParts[i];
            type.setValueTo(detail, value);
        }

        // 건수 설정
        detail.setCount(payments.size());

        return detail;
    }
}
