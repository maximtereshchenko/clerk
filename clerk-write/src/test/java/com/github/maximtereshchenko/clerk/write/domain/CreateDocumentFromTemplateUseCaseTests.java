package com.github.maximtereshchenko.clerk.write.domain;

import com.github.maximtereshchenko.clerk.event.DocumentCreated;
import com.github.maximtereshchenko.clerk.write.api.ClerkWriteModule;
import com.github.maximtereshchenko.clerk.write.api.exception.CouldNotFindTemplate;
import com.github.maximtereshchenko.clerk.write.api.exception.ValuesAreRequired;
import com.github.maximtereshchenko.clerk.write.api.port.Files;
import com.github.maximtereshchenko.test.ClasspathFileExtension;
import com.github.maximtereshchenko.test.PredictableUUIDExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.time.Clock;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith({ClasspathFileExtension.class, PredictableUUIDExtension.class, ClerkWriteModuleExtension.class})
final class CreateDocumentFromTemplateUseCaseTests extends UseCaseTest {

    @Test
    void givenValuesAreMissing_whenCreateDocument_thenValuesAreRequiredThrown(
            UUID documentId,
            UUID templateId,
            ClerkWriteModule module
    ) {
        assertThatThrownBy(() -> module.createDocument(documentId, templateId, Map.of()))
                .isInstanceOf(ValuesAreRequired.class)
                .hasMessageContaining(documentId.toString());
    }

    @Test
    void givenTemplateDoNotExist_whenCreateDocument_thenCouldNotFindTemplateThrown(
            UUID documentId,
            UUID templateId,
            ClerkWriteModule module
    ) {
        assertThatThrownBy(() -> module.createDocument(documentId, templateId, Map.of("placeholder", "value")))
                .isInstanceOf(CouldNotFindTemplate.class)
                .hasMessageContaining(documentId.toString())
                .hasMessageContaining(templateId.toString());
    }

    @Test
    void givenTemplateExists_whenCreateDocument_thenDocumentIsPersisted(
            Files files,
            UUID fileId,
            Path template,
            UUID templateId,
            UUID documentId,
            ClerkWriteModule module
    ) throws Exception {
        persistFile(files, fileId, template);
        module.createTemplate(templateId, fileId, "name");

        module.createDocument(documentId, templateId, Map.of("placeholder", "value"));

        assertThat(files.inputStream(documentId))
                .asString(StandardCharsets.UTF_8)
                .isEqualToIgnoringNewLines("value");
    }

    @Test
    void givenDocumentCreated_whenCreateDocument_thenDocumentCreatedEventPublished(
            Files files,
            UUID fileId,
            Path template,
            UUID templateId,
            UUID documentId,
            EventBusInMemory eventBus,
            Clock clock,
            ClerkWriteModule module
    ) throws Exception {
        persistFile(files, fileId, template);
        module.createTemplate(templateId, fileId, "name");
        eventBus.clear();

        module.createDocument(documentId, templateId, Map.of("placeholder", "value"));

        assertThat(eventBus.published())
                .containsExactly(
                        new DocumentCreated(
                                documentId,
                                clock.instant().plus(1, ChronoUnit.DAYS),
                                clock.instant()
                        )
                );
    }
}