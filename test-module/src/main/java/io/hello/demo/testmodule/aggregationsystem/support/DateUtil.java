package io.hello.demo.testmodule.aggregationsystem.support;

import io.hello.demo.testmodule.aggregationsystem.domain.Period;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateUtil {

    private static final DateTimeFormatter DAILY_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter WEEKLY_FORMATTER = DateTimeFormatter.ofPattern("yyyy-'W'ww");
    private static final DateTimeFormatter MONTHLY_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM");

    public static String formatDateByPeriod(LocalDateTime dateTime, Period period) {
        if (Period.DAILY.equals(period)) {
            return dateTime.format(DAILY_FORMATTER);
        } else if (Period.WEEKLY.equals(period)) {
            return dateTime.format(WEEKLY_FORMATTER);
        } else if (Period.MONTHLY.equals(period)) {
            return dateTime.format(MONTHLY_FORMATTER);
        } else {
            throw new IllegalArgumentException("Unsupported period: " + period);
        }
    }

    public static LocalDateTime getStartOfDay(LocalDateTime dateTime) {
        return dateTime.toLocalDate().atStartOfDay();
    }

    public static LocalDateTime getEndOfDay(LocalDateTime dateTime) {
        return dateTime.toLocalDate().atTime(23, 59, 59, 999999999);
    }
}
