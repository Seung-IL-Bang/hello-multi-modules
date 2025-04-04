package io.hello.demo.testmodule.aggregationsystem.api.v1.response;

import io.hello.demo.testmodule.aggregationsystem.domain.Period;
import io.hello.demo.testmodule.aggregationsystem.domain.PeriodData;
import io.hello.demo.testmodule.aggregationsystem.domain.StatisticsResult;
import io.hello.demo.testmodule.aggregationsystem.domain.StatisticsType;

import java.util.List;

public class StatisticsResponseDto {

    private StatisticsType statisticType;
    private Period period;
    private List<PeriodData> data;

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

    public List<PeriodData> getData() {
        return data;
    }

    public void setData(List<PeriodData> data) {
        this.data = data;
    }

    public static StatisticsResponseDto of(StatisticsResult result) {
        StatisticsResponseDto response = new StatisticsResponseDto();
        response.setStatisticType(result.getStatisticType());
        response.setPeriod(result.getPeriod());
        response.setData(result.getData());
        return response;
    }
}
