package com.github.maximtereshchenko.clerk.read.domain;

import com.github.maximtereshchenko.clerk.event.TemplateCreated;
import com.github.maximtereshchenko.clerk.read.api.ClerkReadModule;
import com.github.maximtereshchenko.clerk.read.api.PlaceholdersPresentation;
import com.github.maximtereshchenko.clerk.read.api.TemplatePresentation;
import com.github.maximtereshchenko.clerk.read.api.exception.TemplateBelongsToAnotherUser;
import com.github.maximtereshchenko.test.PredictableUUIDExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith({PredictableUUIDExtension.class, ClerkReadModuleExtension.class})
final class OnTemplateCreatedUseCaseTests {

    @Test
    void givenTemplatedCreatedEvent_whenOnTemplateCreated_thenTemplateCanBeViewed(
            UUID id,
            UUID userId,
            ClerkReadModule module
    ) {
        module.onTemplateCreated(new TemplateCreated(id, userId, "name", Set.of("placeholder"), Instant.MIN));

        assertThat(module.templates(userId))
                .containsExactly(new TemplatePresentation(id, userId, "name", Instant.MIN));
    }

    @Test
    void givenMultipleTemplates_whenTemplates_thenOnlyTemplatesWhichBelongToUserCanBeViewed(
            UUID id,
            UUID otherId,
            UUID userId,
            UUID otherUserId,
            ClerkReadModule module
    ) {
        module.onTemplateCreated(new TemplateCreated(id, userId, "name", Set.of("placeholder"), Instant.MIN));
        module.onTemplateCreated(
                new TemplateCreated(otherId, otherUserId, "name", Set.of("placeholder"), Instant.MIN)
        );

        assertThat(module.templates(userId))
                .containsExactly(new TemplatePresentation(id, userId, "name", Instant.MIN));
        assertThat(module.templates(otherUserId))
                .containsExactly(new TemplatePresentation(otherId, otherUserId, "name", Instant.MIN));
    }

    @Test
    void givenOldEvent_whenOnTemplateCreated_thenTemplateIsNotChanged(UUID id, UUID userId, ClerkReadModule module) {
        module.onTemplateCreated(new TemplateCreated(id, userId, "name", Set.of("placeholder"), Instant.MAX));
        module.onTemplateCreated(new TemplateCreated(id, userId, "updated", Set.of("placeholder"), Instant.MIN));

        assertThat(module.templates(userId))
                .containsExactly(new TemplatePresentation(id, userId, "name", Instant.MAX));
    }

    @Test
    void givenTemplatedCreatedEvent_whenOnTemplateCreated_thenPlaceholdersCanBeViewed(
            UUID id,
            UUID userId,
            ClerkReadModule module
    ) throws Exception {
        module.onTemplateCreated(new TemplateCreated(id, userId, "name", Set.of("placeholder"), Instant.MIN));

        assertThat(module.placeholders(id, userId))
                .isEqualTo(new PlaceholdersPresentation(id, userId, Set.of("placeholder"), Instant.MIN));
    }

    @Test
    void givenTemplateBelongsToAnotherUser_whenPlaceholders_thenThenTemplateBelongsToAnotherUserThrown(
            UUID id,
            UUID otherUserId,
            UUID userId,
            ClerkReadModule module
    ) {
        module.onTemplateCreated(new TemplateCreated(id, otherUserId, "name", Set.of("placeholder"), Instant.MIN));

        assertThatThrownBy(() -> module.placeholders(id, userId))
                .isInstanceOf(TemplateBelongsToAnotherUser.class)
                .hasMessageContaining(id.toString())
                .hasMessageContaining(otherUserId.toString());
    }

    @Test
    void givenOldEvent_whenOnTemplateCreated_thenPlaceholdersAreNotChanged(
            UUID id,
            UUID userId,
            ClerkReadModule module
    ) throws Exception {
        module.onTemplateCreated(new TemplateCreated(id, userId, "name", Set.of("placeholder"), Instant.MAX));
        module.onTemplateCreated(new TemplateCreated(id, userId, "name", Set.of("updated"), Instant.MIN));

        assertThat(module.placeholders(id, userId))
                .isEqualTo(new PlaceholdersPresentation(id, userId, Set.of("placeholder"), Instant.MAX));
    }
}