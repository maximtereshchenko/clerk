package com.github.maximtereshchenko.clerk.write.domain;

import com.github.maximtereshchenko.clerk.write.api.ClerkWriteModule;
import com.github.maximtereshchenko.clerk.write.api.exception.CouldNotFindTemplate;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith({ClasspathFileExtension.class, PredictableUUIDExtension.class, ClerkWriteModuleExtension.class})
final class CreateDocumentFromTemplateUseCaseTests {

    @Test
    void givenTemplateDoNotExist_whenCreateDocument_thenCouldNotFindTemplateThrown(
            UUID documentId,
            UUID templateId,
            ClerkWriteModule module
    ) {
        assertThatThrownBy(() -> module.createDocument(documentId, templateId, Map.of()))
                .isInstanceOf(CouldNotFindTemplate.class)
                .hasMessageContaining(documentId.toString())
                .hasMessageContaining(templateId.toString());
    }
}