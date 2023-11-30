package com.github.maximtereshchenko.clerk.read.domain;

import com.github.maximtereshchenko.clerk.event.DocumentCreated;
import com.github.maximtereshchenko.clerk.read.api.ClerkReadModule;
import com.github.maximtereshchenko.clerk.read.api.DocumentPresentation;
import com.github.maximtereshchenko.test.PredictableUUIDExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.time.Instant;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith({PredictableUUIDExtension.class, ClerkReadModuleExtension.class})
final class OnDocumentCreatedUseCaseTests {

    @Test
    void givenDocumentCreatedEvent_whenOnDocumentCreated_thenDocumentCanBeViewed(
            UUID fileId,
            UUID userId,
            ClerkReadModule module
    ) {
        module.onDocumentCreated(new DocumentCreated(fileId, userId, Instant.MAX, Instant.MIN));

        assertThat(module.documents(userId))
                .containsExactly(new DocumentPresentation(fileId, Instant.MAX, Instant.MIN));
    }

    @Test
    void givenOldEvent_whenOnDocumentCreated_thenDocumentISNotChanged(
            UUID fileId,
            UUID userId,
            ClerkReadModule module
    ) {
        module.onDocumentCreated(new DocumentCreated(fileId, userId, Instant.MAX, Instant.MAX));
        module.onDocumentCreated(new DocumentCreated(fileId, userId, Instant.MIN, Instant.MIN));

        assertThat(module.documents(userId)).containsExactly(new DocumentPresentation(fileId, Instant.MAX, Instant.MAX));
    }
}