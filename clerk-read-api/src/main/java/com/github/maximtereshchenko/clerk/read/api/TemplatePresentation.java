package com.github.maximtereshchenko.clerk.read.api;

import java.time.Instant;
import java.util.UUID;

public record TemplatePresentation(UUID id, UUID userId, String name, Instant timestamp) {}
