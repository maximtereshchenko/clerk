package com.github.maximtereshchenko.clerk.write.domain;

import com.github.maximtereshchenko.clerk.event.TemplateCreated;
import com.github.maximtereshchenko.clerk.write.api.ClerkWriteModule;
import com.github.maximtereshchenko.clerk.write.api.exception.IdIsTaken;
import com.github.maximtereshchenko.clerk.write.api.exception.NameIsRequired;
import com.github.maximtereshchenko.clerk.write.api.exception.TemplateIsEmpty;
import com.github.maximtereshchenko.clerk.write.api.port.Files;
import com.github.maximtereshchenko.clerk.write.api.port.PersistentTemplate;
import com.github.maximtereshchenko.clerk.write.api.port.Templates;
import com.github.maximtereshchenko.clerk.write.api.port.exception.FileBelongsToAnotherUser;
import com.github.maximtereshchenko.test.ClasspathResource;
import com.github.maximtereshchenko.test.PredictableUUIDExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.nio.file.Path;
import java.time.Clock;
import java.time.Instant;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith({PredictableUUIDExtension.class, ClerkWriteModuleExtension.class})
final class CreateTemplateUseCaseTests {

    @Test
    void givenFileContainsPlaceholders_whenCreateTemplate_thenTemplatePersisted(
            Files files,
            UUID fileId,
            UUID userId,
            @ClasspathResource Path template,
            UUID templateId,
            Templates templates,
            ClerkWriteModule module
    ) throws Exception {
        files.persist(fileId, userId, Instant.MIN, java.nio.file.Files.readAllBytes(template));

        module.createTemplate(templateId, userId, fileId, "name");

        assertThat(templates.findById(templateId))
                .contains(
                        new PersistentTemplate(
                                templateId,
                                userId,
                                fileId,
                                "name",
                                Set.of("placeholder")
                        )
                );
    }

    @Test
    void givenTemplateIsEmpty_whenCreateTemplate_thenTemplateIsEmptyThrown(
            Files files,
            UUID fileId,
            UUID userId,
            @ClasspathResource Path emptyTemplate,
            UUID templateId,
            ClerkWriteModule module
    ) throws Exception {
        files.persist(fileId, userId, Instant.MIN, java.nio.file.Files.readAllBytes(emptyTemplate));

        assertThatThrownBy(() -> module.createTemplate(templateId, userId, fileId, "name"))
                .isInstanceOf(TemplateIsEmpty.class)
                .hasMessageContaining(templateId.toString())
                .hasMessageContaining(fileId.toString());
    }

    @Test
    void givenTemplateIsCreated_whenCreateTemplate_thenTemplateCreatedEventPublished(
            Files files,
            UUID fileId,
            UUID userId,
            @ClasspathResource Path template,
            UUID templateId,
            EventBusInMemory eventBus,
            Clock clock,
            ClerkWriteModule module
    ) throws Exception {
        files.persist(fileId, userId, Instant.MIN, java.nio.file.Files.readAllBytes(template));

        module.createTemplate(templateId, userId, fileId, "name");

        assertThat(eventBus.published())
                .containsExactly(
                        new TemplateCreated(
                                templateId,
                                userId,
                                "name",
                                Set.of("placeholder"),
                                clock.instant()
                        )
                );
    }

    @Test
    void givenNameIsMissing_whenCreateTemplate_thenNameIsRequiredThrown(
            UUID templateId,
            UUID userId,
            UUID fileId,
            ClerkWriteModule module
    ) {
        assertThatThrownBy(() -> module.createTemplate(templateId, userId, fileId, ""))
                .isInstanceOf(NameIsRequired.class)
                .hasMessage(templateId.toString());
    }

    @Test
    void givenTemplateExists_whenCreateTemplate_thenIdIsTakenThrown(
            Files files,
            UUID fileId,
            UUID userId,
            @ClasspathResource Path template,
            UUID templateId,
            ClerkWriteModule module
    ) throws Exception {
        files.persist(fileId, userId, Instant.MIN, java.nio.file.Files.readAllBytes(template));
        module.createTemplate(templateId, userId, fileId, "name");

        assertThatThrownBy(() -> module.createTemplate(templateId, userId, fileId, "different name"))
                .isInstanceOf(IdIsTaken.class)
                .hasMessage(templateId.toString());
    }

    @Test
    void givenFileBelongsToAnotherUser_whenCreateTemplate_thenFileBelongsToAnotherUserThrown(
            Files files,
            UUID fileId,
            UUID otherUserId,
            UUID userId,
            @ClasspathResource Path template,
            UUID templateId,
            ClerkWriteModule module
    ) throws Exception {
        files.persist(fileId, otherUserId, Instant.MIN, java.nio.file.Files.readAllBytes(template));

        assertThatThrownBy(() -> module.createTemplate(templateId, userId, fileId, "name"))
                .isInstanceOf(FileBelongsToAnotherUser.class)
                .hasMessageContaining(fileId.toString())
                .hasMessageContaining(otherUserId.toString());
    }
}