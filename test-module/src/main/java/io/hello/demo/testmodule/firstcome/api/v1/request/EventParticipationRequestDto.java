package io.hello.demo.testmodule.firstcome.api.v1.request;

import io.hello.demo.testmodule.firstcome.domain.EventParticipationRequest;

import java.time.LocalDateTime;

public class EventParticipationRequestDto {

    private String eventId;
    private String userId;
    private LocalDateTime timestamp;

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

    public EventParticipationRequest toDomain() {
        return new EventParticipationRequest(eventId, userId, timestamp);
    }
}
