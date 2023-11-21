package com.github.maximtereshchenko.clerk.write.api.port;

import com.github.maximtereshchenko.clerk.event.Event;

public interface EventBus {

    void publish(Event event);
}
