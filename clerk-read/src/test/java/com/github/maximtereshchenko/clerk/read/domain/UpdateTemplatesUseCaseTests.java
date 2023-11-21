package com.github.maximtereshchenko.clerk.read.domain;

import com.github.maximtereshchenko.clerk.event.TemplateCreated;
import com.github.maximtereshchenko.clerk.read.api.ClerkReadModule;
import com.github.maximtereshchenko.clerk.read.api.TemplatePresentation;
import com.github.maximtereshchenko.clerk.write.eventstore.inmemory.TemplatesInMemory;
import com.github.maximtereshchenko.test.PredictableUUIDExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(PredictableUUIDExtension.class)
final class UpdateTemplatesUseCaseTests {

    private final ClerkReadModule module = new ClerkReadFacade(new TemplatesInMemory());

    @Test
    void givenTemplatedCreatedEvent_whenUpdateTemplates_thenItCanBeViewed(UUID id) {
        module.onTemplateCreated(new TemplateCreated(id, "name", Set.of("placeholder"), Instant.MIN));

        assertThat(module.templates()).containsExactly(new TemplatePresentation(id, "name", Instant.MIN));
    }

    @Test
    void givenOldEvent_whenUpdateTemplates_thenPresentationIsNotUpdated(UUID id) {
        module.onTemplateCreated(new TemplateCreated(id, "name", Set.of("placeholder"), Instant.MAX));
        module.onTemplateCreated(new TemplateCreated(id, "updated", Set.of("placeholder"), Instant.MIN));

        assertThat(module.templates()).containsExactly(new TemplatePresentation(id, "name", Instant.MAX));
    }
}