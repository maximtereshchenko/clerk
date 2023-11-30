package com.github.maximtereshchenko.clerk.event;

import java.time.Instant;
import java.util.UUID;

public record DocumentCreated(
        UUID fileId,
        UUID userId,
        Instant timeToLive,
        Instant timestamp
) implements Event {}
