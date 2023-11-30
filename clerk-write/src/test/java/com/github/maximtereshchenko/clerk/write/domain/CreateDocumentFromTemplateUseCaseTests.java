package com.github.maximtereshchenko.clerk.write.domain;

import com.github.maximtereshchenko.clerk.event.DocumentCreated;
import com.github.maximtereshchenko.clerk.write.api.ClerkWriteModule;
import com.github.maximtereshchenko.clerk.write.api.exception.CouldNotFindTemplate;
import com.github.maximtereshchenko.clerk.write.api.exception.TemplateBelongsToAnotherUser;
import com.github.maximtereshchenko.clerk.write.api.exception.ValuesAreRequired;
import com.github.maximtereshchenko.clerk.write.api.port.Files;
import com.github.maximtereshchenko.test.ClasspathFileExtension;
import com.github.maximtereshchenko.test.PredictableUUIDExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.time.Clock;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith({ClasspathFileExtension.class, PredictableUUIDExtension.class, ClerkWriteModuleExtension.class})
final class CreateDocumentFromTemplateUseCaseTests {

    @Test
    void givenValuesAreMissing_whenCreateDocument_thenValuesAreRequiredThrown(
            UUID documentId,
            UUID userId,
            UUID templateId,
            ClerkWriteModule module
    ) {
        var values = Map.<String, String>of();

        assertThatThrownBy(() -> module.createDocument(documentId, userId, templateId, values))
                .isInstanceOf(ValuesAreRequired.class)
                .hasMessageContaining(documentId.toString());
    }

    @Test
    void givenTemplateDoNotExist_whenCreateDocument_thenCouldNotFindTemplateThrown(
            UUID documentId,
            UUID userId,
            UUID templateId,
            ClerkWriteModule module
    ) {
        var values = Map.of("placeholder", "value");

        assertThatThrownBy(() -> module.createDocument(documentId, userId, templateId, values))
                .isInstanceOf(CouldNotFindTemplate.class)
                .hasMessageContaining(documentId.toString())
                .hasMessageContaining(templateId.toString());
    }

    @Test
    void givenTemplateExists_whenCreateDocument_thenDocumentIsPersisted(
            Files files,
            UUID fileId,
            UUID userId,
            Path template,
            UUID templateId,
            UUID documentId,
            ClerkWriteModule module
    ) throws Exception {
        files.persist(fileId, userId, Instant.MIN, java.nio.file.Files.readAllBytes(template));
        module.createTemplate(templateId, userId, fileId, "name");

        module.createDocument(documentId, userId, templateId, Map.of("placeholder", "value"));

        assertThat(files.inputStream(documentId, userId))
                .asString(StandardCharsets.UTF_8)
                .isEqualToIgnoringNewLines("value");
    }

    @Test
    void givenDocumentCreated_whenCreateDocument_thenDocumentCreatedEventPublished(
            Files files,
            UUID fileId,
            UUID userId,
            Path template,
            UUID templateId,
            UUID documentId,
            EventBusInMemory eventBus,
            Clock clock,
            ClerkWriteModule module
    ) throws Exception {
        files.persist(fileId, userId, Instant.MIN, java.nio.file.Files.readAllBytes(template));
        module.createTemplate(templateId, userId, fileId, "name");
        eventBus.clear();

        module.createDocument(documentId, userId, templateId, Map.of("placeholder", "value"));

        assertThat(eventBus.published())
                .containsExactly(
                        new DocumentCreated(
                                documentId,
                                userId,
                                clock.instant().plus(1, ChronoUnit.DAYS),
                                clock.instant()
                        )
                );
    }

    @Test
    void givenTemplateBelongsToAnotherUser_whenCreateDocument_thenDocumentCreatedEventPublished(
            Files files,
            UUID fileId,
            UUID otherUserId,
            UUID userId,
            Path template,
            UUID templateId,
            UUID documentId,
            ClerkWriteModule module
    ) throws Exception {
        files.persist(fileId, otherUserId, Instant.MIN, java.nio.file.Files.readAllBytes(template));
        module.createTemplate(templateId, otherUserId, fileId, "name");
        var values = Map.of("placeholder", "value");

        assertThatThrownBy(() -> module.createDocument(documentId, userId, templateId, values))
                .isInstanceOf(TemplateBelongsToAnotherUser.class)
                .hasMessageContaining(templateId.toString())
                .hasMessageContaining(otherUserId.toString());
    }
}