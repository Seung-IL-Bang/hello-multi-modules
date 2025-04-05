package io.hello.demo.testmodule.firstcome.domain;

import java.time.LocalDateTime;

public class EventParticipationRequest {

    private String eventId;
    private String userId;
    private LocalDateTime timestamp;

    public EventParticipationRequest(String eventId, String userId, LocalDateTime timestamp) {
        this.eventId = eventId;
        this.userId = userId;
        this.timestamp = timestamp;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
