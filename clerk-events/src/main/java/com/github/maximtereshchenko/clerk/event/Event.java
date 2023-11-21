package com.github.maximtereshchenko.clerk.event;

import java.time.Instant;

public sealed interface Event permits DocumentCreated, TemplateCreated {

    Instant timestamp();
}
