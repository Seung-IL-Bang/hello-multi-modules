package io.hello.demo.testmodule.firstcome.domain;

import io.hello.demo.testmodule.firstcome.storage.Event;
import io.hello.demo.testmodule.firstcome.storage.EventStatus;

public class PendingEventStrategy implements EventProcessingStrategy {
    @Override
    public EventParticipationResult process(Event event, EventParticipationRequest request) {
        return EventParticipationResult.failure("이벤트가 아직 시작되지 않았습니다. 시작 시간: " + event.getStartTime());
    }

    @Override
    public boolean canProcess(Event event) {
        return event.getStatus() == EventStatus.PENDING;
    }
}
