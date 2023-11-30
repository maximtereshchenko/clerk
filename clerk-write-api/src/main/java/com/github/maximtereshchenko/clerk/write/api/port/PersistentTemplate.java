package com.github.maximtereshchenko.clerk.write.api.port;

import java.util.Set;
import java.util.UUID;

public record PersistentTemplate(
        UUID id,
        UUID userId,
        UUID fileId,
        String name,
        Set<String> placeholders
) {}
