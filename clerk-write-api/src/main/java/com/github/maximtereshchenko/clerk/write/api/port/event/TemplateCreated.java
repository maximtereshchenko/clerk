package com.github.maximtereshchenko.clerk.write.api.port.event;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;

public record TemplateCreated(
        UUID aggregateId,
        String name,
        Set<String> placeholders,
        Instant timestamp
) implements Event {}
