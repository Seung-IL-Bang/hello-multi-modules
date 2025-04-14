package io.hello.demo.idempotentmodule;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class EventStore {

    private final Map<String, MoneyWithdrawnEvent> eventStore = new ConcurrentHashMap<>();

    public void append(MoneyWithdrawnEvent event) {
        if (!exists(event.eventId())) {
            eventStore.put(event.eventId(), event);
        }
    }

    public MoneyWithdrawnEvent get(String eventId) {
        return eventStore.get(eventId);
    }

    public boolean exists(String eventId) {
        return eventStore.containsKey(eventId);
    }

}
