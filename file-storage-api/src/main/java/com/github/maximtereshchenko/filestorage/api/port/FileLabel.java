package com.github.maximtereshchenko.filestorage.api.port;

import java.time.Instant;
import java.util.UUID;

public record FileLabel(UUID id, Instant timeToLive) {}
