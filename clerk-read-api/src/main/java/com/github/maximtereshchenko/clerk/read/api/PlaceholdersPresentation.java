package com.github.maximtereshchenko.clerk.read.api;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;

public record PlaceholdersPresentation(UUID id, Set<String> placeholders, Instant timestamp) {}
