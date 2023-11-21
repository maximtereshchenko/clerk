package com.github.maximtereshchenko.clerk.write.domain;

import com.github.maximtereshchenko.clerk.write.api.ClerkWriteModule;
import com.github.maximtereshchenko.clerk.write.api.exception.CouldNotExtendTimeToLive;
import com.github.maximtereshchenko.clerk.write.api.exception.CouldNotFindPlaceholders;
import com.github.maximtereshchenko.clerk.write.api.exception.TemplateIsEmpty;
import com.github.maximtereshchenko.clerk.write.api.port.event.TemplateCreated;
import com.github.maximtereshchenko.clerk.write.api.port.event.integration.TemplateCreatedIntegrationEvent;
import com.github.maximtereshchenko.clerk.write.api.port.exception.CouldNotFindFile;
import com.github.maximtereshchenko.clerk.write.api.port.exception.CouldNotReadInputStream;
import com.github.maximtereshchenko.clerk.write.domain.ClerkWriteFacade.Builder;
import com.github.maximtereshchenko.eventstore.inmemory.EventStoreInMemory;
import com.github.maximtereshchenko.files.inmemory.FilesInMemory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.nio.file.Path;
import java.time.Clock;
import java.time.Instant;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith({ClasspathFileExtension.class, PredictableUUIDExtension.class, ClerkWriteModuleExtension.class})
final class CreateTemplateUseCaseTests {

    @Test
    void givenFileExists_whenCreateTemplate_thenFileTimeToLiveExtended(
            FilesInMemory files,
            UUID fileId,
            Path template,
            UUID templateId,
            ClerkWriteModule module
    ) throws Exception {
        files.save(fileId, template, Instant.MIN);

        module.createTemplate(templateId, fileId, "name");

        assertThat(files.timeToLive(fileId)).isEqualTo(Instant.MAX);
    }

    @Test
    void givenFileDoNotExist_whenCreateTemplate_thenCouldNotExtendTimeToLiveThrown(
            UUID fileId,
            UUID templateId,
            ClerkWriteModule module
    ) {
        assertThatThrownBy(() -> module.createTemplate(templateId, fileId, "name"))
                .isInstanceOf(CouldNotExtendTimeToLive.class)
                .hasMessageContaining(templateId.toString())
                .hasMessageContaining(fileId.toString())
                .hasCauseInstanceOf(CouldNotFindFile.class);
    }

    @Test
    void givenFileContainsPlaceholders_whenCreateTemplate_thenTemplateCreatedEventSaved(
            FilesInMemory files,
            UUID fileId,
            Path template,
            UUID templateId,
            EventStoreInMemory eventStore,
            Clock clock,
            ClerkWriteModule module
    ) throws Exception {
        files.save(fileId, template, Instant.MIN);

        module.createTemplate(templateId, fileId, "name");

        assertThat(eventStore.events(templateId))
                .containsExactly(
                        new TemplateCreated(
                                templateId,
                                "name",
                                Set.of("placeholder"),
                                1,
                                clock.instant()
                        )
                );
    }

    @Test
    void givenTemplateEngineFailed_whenCreateTemplate_thenCouldNotFindPlaceholdersThrown(
            FilesInMemory files,
            UUID fileId,
            Path template,
            UUID templateId,
            Builder builder
    ) {
        files.save(fileId, template, Instant.MIN);

        var module = builder.withTemplateEngine(new ExceptionThrowingTemplateEngine())
                .build();

        assertThatThrownBy(() -> module.createTemplate(templateId, fileId, "name"))
                .isInstanceOf(CouldNotFindPlaceholders.class)
                .hasMessageContaining(templateId.toString())
                .hasMessageContaining(fileId.toString())
                .hasCauseInstanceOf(CouldNotReadInputStream.class);
    }

    @Test
    void givenTemplateIsEmpty_whenCreateTemplate_thenTemplateIsEmptyThrown(
            FilesInMemory files,
            UUID fileId,
            Path emptyTemplate,
            UUID templateId,
            ClerkWriteModule module
    ) {
        files.save(fileId, emptyTemplate, Instant.MIN);

        assertThatThrownBy(() -> module.createTemplate(templateId, fileId, "name"))
                .isInstanceOf(TemplateIsEmpty.class)
                .hasMessageContaining(templateId.toString())
                .hasMessageContaining(fileId.toString());
    }

    @Test
    void givenTemplateIsCreated_whenCreateTemplate_thenTemplateCreatedIntegrationEventPublished(
            FilesInMemory files,
            UUID fileId,
            Path template,
            UUID templateId,
            EventBusInMemory eventBus,
            Clock clock,
            ClerkWriteModule module
    ) throws Exception {
        files.save(fileId, template, Instant.MIN);

        module.createTemplate(templateId, fileId, "name");

        assertThat(eventBus.published())
                .containsExactly(
                        new TemplateCreatedIntegrationEvent(
                                templateId,
                                "name",
                                Set.of("placeholder"),
                                1,
                                clock.instant()
                        )
                );
    }
}