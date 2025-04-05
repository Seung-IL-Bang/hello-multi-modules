package io.hello.demo.testmodule.firstcome.domain;

import io.hello.demo.testmodule.firstcome.storage.Event;

// 이벤트 상태에 따른 처리 전략 인터페이스
public interface EventProcessingStrategy {
    EventParticipationResult process(Event event, EventParticipationRequest request);
    boolean canProcess(Event event);
}
