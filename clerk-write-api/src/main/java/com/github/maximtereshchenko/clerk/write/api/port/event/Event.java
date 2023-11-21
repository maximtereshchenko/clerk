package com.github.maximtereshchenko.clerk.write.api.port.event;

import java.time.Instant;

public sealed interface Event permits DocumentCreated, TemplateCreated {

    Instant timestamp();
}
