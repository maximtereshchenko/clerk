package com.github.maximtereshchenko.clerk.write.api.port.event;

import java.time.Instant;
import java.util.UUID;

public sealed interface Event permits TemplateCreated {

    UUID aggregateId();

    long version();

    Instant timestamp();
}
