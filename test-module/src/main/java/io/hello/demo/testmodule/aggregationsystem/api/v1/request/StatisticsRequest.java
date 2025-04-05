package io.hello.demo.testmodule.aggregationsystem.api.v1.request;

import io.hello.demo.testmodule.aggregationsystem.domain.StatisticsGroupType;
import io.hello.demo.testmodule.aggregationsystem.domain.StatisticsType;

import java.time.LocalDate;
import java.util.List;

public class StatisticsRequest {
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
}
