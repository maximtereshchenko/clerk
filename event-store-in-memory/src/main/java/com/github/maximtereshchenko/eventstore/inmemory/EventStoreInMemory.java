package com.github.maximtereshchenko.eventstore.inmemory;

import com.github.maximtereshchenko.clerk.write.api.port.EventStore;
import com.github.maximtereshchenko.clerk.write.api.port.event.Event;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public final class EventStoreInMemory implements EventStore {

    private final Map<UUID, List<Event>> events = new HashMap<>();

    @Override
    public synchronized void persist(Event event) {
        events.computeIfAbsent(event.aggregateId(), key -> new ArrayList<>()).add(event);
    }

    public List<Event> events(UUID aggregateId) {
        return List.copyOf(events.getOrDefault(aggregateId, List.of()));
    }
}
