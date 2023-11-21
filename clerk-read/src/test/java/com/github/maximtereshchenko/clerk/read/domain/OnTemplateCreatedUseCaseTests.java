package com.github.maximtereshchenko.clerk.read.domain;

import com.github.maximtereshchenko.clerk.event.TemplateCreated;
import com.github.maximtereshchenko.clerk.read.api.ClerkReadModule;
import com.github.maximtereshchenko.clerk.read.api.PlaceholdersPresentation;
import com.github.maximtereshchenko.clerk.read.api.TemplatePresentation;
import com.github.maximtereshchenko.clerk.read.placeholders.inmemory.PlaceholdersInMemory;
import com.github.maximtereshchenko.clerk.read.templates.inmemory.TemplatesInMemory;
import com.github.maximtereshchenko.test.PredictableUUIDExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(PredictableUUIDExtension.class)
final class OnTemplateCreatedUseCaseTests {

    private final ClerkReadModule module = new ClerkReadFacade(new TemplatesInMemory(), new PlaceholdersInMemory());

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

    @Test
    void givenTemplatedCreatedEvent_whenUpdatePlaceholders_thenTheyCanBeViewed(UUID id) {
        module.onTemplateCreated(new TemplateCreated(id, "name", Set.of("placeholder"), Instant.MIN));

        assertThat(module.placeholders(id))
                .isEqualTo(new PlaceholdersPresentation(id, Set.of("placeholder"), Instant.MIN));
    }

    @Test
    void givenOldEvent_whenUpdatePlaceholders_thenPresentationIsNotUpdated(UUID id) {
        module.onTemplateCreated(new TemplateCreated(id, "name", Set.of("placeholder"), Instant.MAX));
        module.onTemplateCreated(new TemplateCreated(id, "name", Set.of("updated"), Instant.MIN));

        assertThat(module.placeholders(id))
                .isEqualTo(new PlaceholdersPresentation(id, Set.of("placeholder"), Instant.MAX));
    }
}