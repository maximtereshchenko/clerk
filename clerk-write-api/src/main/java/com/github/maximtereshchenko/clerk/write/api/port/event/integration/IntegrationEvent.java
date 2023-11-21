package com.github.maximtereshchenko.clerk.write.api.port.event.integration;

import java.time.Instant;
import java.util.UUID;

public sealed interface IntegrationEvent permits TemplateCreatedIntegrationEvent {

    UUID aggregateId();

    long version();

    Instant timestamp();
}
