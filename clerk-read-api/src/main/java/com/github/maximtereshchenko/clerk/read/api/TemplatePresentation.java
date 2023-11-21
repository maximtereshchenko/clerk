package com.github.maximtereshchenko.clerk.read.api;

import java.time.Instant;
import java.util.UUID;

public record TemplatePresentation(UUID id, String name, Instant timestamp) {}
