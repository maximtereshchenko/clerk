package com.github.maximtereshchenko.clerk.write.api.port;

import com.github.maximtereshchenko.clerk.write.api.port.event.Event;

public interface EventStore {

    void persist(Event event);
}
