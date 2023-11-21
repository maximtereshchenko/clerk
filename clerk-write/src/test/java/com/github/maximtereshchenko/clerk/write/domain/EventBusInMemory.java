package com.github.maximtereshchenko.clerk.write.domain;

import com.github.maximtereshchenko.clerk.write.api.port.EventBus;
import com.github.maximtereshchenko.clerk.write.api.port.event.integration.IntegrationEvent;

import java.util.ArrayList;
import java.util.List;

final class EventBusInMemory implements EventBus {

    private final List<IntegrationEvent> published = new ArrayList<>();

    @Override
    public void publish(IntegrationEvent event) {
        published.add(event);
    }

    List<IntegrationEvent> published() {
        return List.copyOf(published);
    }
}
