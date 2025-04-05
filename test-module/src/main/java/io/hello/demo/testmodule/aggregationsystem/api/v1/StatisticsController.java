package io.hello.demo.testmodule.aggregationsystem.api.v1;

import io.hello.demo.testmodule.aggregationsystem.domain.StatisticsResult;
import io.hello.demo.testmodule.aggregationsystem.domain.StatisticsService;
import io.hello.demo.testmodule.aggregationsystem.api.v1.request.StatisticsRequestDto;
import io.hello.demo.testmodule.aggregationsystem.api.v1.response.StatisticsResponseDto;

public class StatisticsController {

    private final StatisticsService statisticsService;

    public StatisticsController(StatisticsService statisticsService) {
        this.statisticsService = statisticsService;
    }

    public StatisticsResponseDto getStatistics(StatisticsRequestDto request) {
        StatisticsResult result = statisticsService.getStatistics(request.toDomain());
        return StatisticsResponseDto.of(result);
    }
}
