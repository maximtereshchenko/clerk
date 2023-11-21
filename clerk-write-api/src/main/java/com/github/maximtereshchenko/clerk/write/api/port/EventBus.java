package com.github.maximtereshchenko.clerk.write.api.port;

import com.github.maximtereshchenko.clerk.write.api.port.event.integration.IntegrationEvent;

public interface EventBus {

    void publish(IntegrationEvent event);
}
