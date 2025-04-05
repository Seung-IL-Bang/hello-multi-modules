package io.hello.demo.testmodule.aggregationsystem.domain;

import java.util.List;

public class PeriodData {
    private String date;
    private List<DetailData> details;

    // Getters and Setters
    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }
    public List<DetailData> getDetails() { return details; }
    public void setDetails(List<DetailData> details) { this.details = details; }
}
