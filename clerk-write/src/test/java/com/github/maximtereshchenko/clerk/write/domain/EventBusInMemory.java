package com.github.maximtereshchenko.clerk.write.domain;

import com.github.maximtereshchenko.clerk.event.Event;
import com.github.maximtereshchenko.clerk.write.api.port.EventBus;

import java.util.ArrayList;
import java.util.List;

final class EventBusInMemory implements EventBus {

    private final List<Event> published = new ArrayList<>();

    @Override
    public void publish(Event event) {
        published.add(event);
    }

    void clear() {
        published.clear();
    }

    List<Event> published() {
        return List.copyOf(published);
    }
}
