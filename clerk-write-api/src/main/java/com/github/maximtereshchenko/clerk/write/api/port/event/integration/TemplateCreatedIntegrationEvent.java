package com.github.maximtereshchenko.clerk.write.api.port.event.integration;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;

public record TemplateCreatedIntegrationEvent(
    UUID aggregateId,
    String name,
    Set<String> placeholders,
    long version,
    Instant timestamp
) implements IntegrationEvent {}
