package com.github.maximtereshchenko.clerk.read.api;

import java.time.Instant;
import java.util.UUID;

public record DocumentPresentation(UUID fileId, UUID userId, Instant timeToLive, Instant timestamp) {}
