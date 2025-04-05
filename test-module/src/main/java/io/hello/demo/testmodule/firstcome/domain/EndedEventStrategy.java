package io.hello.demo.testmodule.firstcome.domain;

import io.hello.demo.testmodule.firstcome.storage.Event;
import io.hello.demo.testmodule.firstcome.storage.EventStatus;

public class EndedEventStrategy implements EventProcessingStrategy {
    @Override
    public EventParticipationResult process(Event event, EventParticipationRequest request) {
        if (event.getRemainingCouponCount().intValue() <= 0) {
            return EventParticipationResult.failure("이벤트 쿠폰이 모두 소진되었습니다.");
        } else {
            return EventParticipationResult.failure("이벤트가 종료되었습니다.");
        }
    }

    @Override
    public boolean canProcess(Event event) {
        return event.getStatus() == EventStatus.ENDED;
    }
}
