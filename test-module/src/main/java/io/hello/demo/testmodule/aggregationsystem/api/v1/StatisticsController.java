package io.hello.demo.testmodule.aggregationsystem.api.v1;

import io.hello.demo.testmodule.aggregationsystem.domain.StatisticsService;
import io.hello.demo.testmodule.aggregationsystem.api.v1.request.StatisticsRequest;
import io.hello.demo.testmodule.aggregationsystem.api.v1.response.StatisticsResponse;

public class StatisticsController {

    private final StatisticsService statisticsService;

    public StatisticsController(StatisticsService statisticsService) {
        this.statisticsService = statisticsService;
    }

    public StatisticsResponse getStatistics(StatisticsRequest request) {
        return statisticsService.getStatistics(request);
    }
}
