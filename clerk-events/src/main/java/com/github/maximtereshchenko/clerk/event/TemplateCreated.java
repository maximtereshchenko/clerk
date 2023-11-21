package com.github.maximtereshchenko.clerk.event;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;

public record TemplateCreated(
        UUID id,
        String name,
        Set<String> placeholders,
        Instant timestamp
) implements Event {}
