package io.hello.demo.testmodule.firstcome.storage;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class InMemoryEventRepository implements EventRepository {

    private final Map<String, Event> eventStore = new ConcurrentHashMap<>();

    @Override
    public Optional<Event> findById(String eventId) {
        return Optional.ofNullable(eventStore.getOrDefault(eventId, null));
    }

    @Override
    public Event save(Event event) {
        eventStore.put(event.getEventId(), event);
        return event;
    }

    @Override
    public List<Event> findActiveEvents() {
        LocalDateTime now = LocalDateTime.now();
        return eventStore.values().stream()
                .filter(event ->
                        event.getStartTime().isBefore(now) &&
                        event.getEndTime().isAfter(now) &&
                        event.getRemainingCouponCount().intValue() > 0)
                .collect(Collectors.toList());
    }
}
