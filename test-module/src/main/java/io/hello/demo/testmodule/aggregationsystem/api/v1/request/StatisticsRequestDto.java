package io.hello.demo.testmodule.aggregationsystem.api.v1.request;

import io.hello.demo.testmodule.aggregationsystem.domain.Period;
import io.hello.demo.testmodule.aggregationsystem.domain.StatisticsGroupType;
import io.hello.demo.testmodule.aggregationsystem.domain.StatisticsRequest;
import io.hello.demo.testmodule.aggregationsystem.domain.StatisticsType;

import java.time.LocalDate;
import java.util.List;

public class StatisticsRequestDto {
    private String merchantId;
    private StatisticsType statisticType; // PAYMENT_AMOUNT, PAYMENT_COUNT, PAYMENT_METHOD_RATIO 등
    private Period period; // DAILY, WEEKLY, MONTHLY
    private LocalDate startDate;
    private LocalDate endDate;
    private List<StatisticsGroupType> groupBy; // PAYMENT_METHOD, PRODUCT_CATEGORY 등

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public StatisticsType getStatisticType() {
        return statisticType;
    }

    public void setStatisticType(StatisticsType statisticType) {
        this.statisticType = statisticType;
    }

    public Period getPeriod() {
        return period;
    }

    public void setPeriod(Period period) {
        this.period = period;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public List<StatisticsGroupType> getGroupBy() {
        return groupBy;
    }

    public void setGroupBy(List<StatisticsGroupType> groupBy) {
        this.groupBy = groupBy;
    }

    public StatisticsRequest toDomain() {

        if (this.statisticType == null) {
            throw new IllegalArgumentException("Statistic type is required");
        }

        if (this.period == null) {
            throw new IllegalArgumentException("Period is required");
        }

        if (this.startDate == null) {
            throw new IllegalArgumentException("Start date is required");
        }

        if (this.endDate == null) {
            throw new IllegalArgumentException("End date is required");
        }

        if (this.startDate.isAfter(this.endDate)) {
            throw new IllegalArgumentException("Start date must be before or equal to end date");
        }

        StatisticsRequest request = new StatisticsRequest();
        request.setMerchantId(merchantId);
        request.setStatisticType(statisticType);
        request.setPeriod(period);
        request.setStartDate(startDate);
        request.setEndDate(endDate);
        request.setGroupBy(groupBy);
        return request;
    }
}
