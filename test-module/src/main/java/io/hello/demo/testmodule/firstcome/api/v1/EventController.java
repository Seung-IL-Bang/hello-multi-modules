package io.hello.demo.testmodule.firstcome.api.v1;

import io.hello.demo.testmodule.firstcome.api.v1.request.EventParticipationRequestDto;
import io.hello.demo.testmodule.firstcome.api.v1.response.EventParticipationResponseDto;
import io.hello.demo.testmodule.firstcome.domain.EventService;

public class EventController {

    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    public EventParticipationResponseDto participateInEvent(EventParticipationRequestDto request) {
        return EventParticipationResponseDto.of(eventService.participateInEvent(request.toDomain()));
    }
}
