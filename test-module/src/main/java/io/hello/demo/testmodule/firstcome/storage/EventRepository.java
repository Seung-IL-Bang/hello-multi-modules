package io.hello.demo.testmodule.firstcome.storage;


import java.util.List;
import java.util.Optional;

// 이벤트 저장소 인터페이스
public interface EventRepository {

    Optional<Event> findById(String eventId);
    Event save(Event event);
    List<Event> findActiveEvents();

}
