package io.hello.demo.testmodule.aggregationsystem.api.v1.response;

import io.hello.demo.testmodule.aggregationsystem.api.v1.request.Period;
import io.hello.demo.testmodule.aggregationsystem.domain.PeriodData;
import io.hello.demo.testmodule.aggregationsystem.domain.StatisticsType;

import java.util.List;

public class StatisticsResponse {

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
}
