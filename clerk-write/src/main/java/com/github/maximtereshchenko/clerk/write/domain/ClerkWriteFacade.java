package com.github.maximtereshchenko.clerk.write.domain;

import com.github.maximtereshchenko.clerk.write.api.ClerkWriteModule;
import com.github.maximtereshchenko.clerk.write.api.exception.*;
import com.github.maximtereshchenko.clerk.write.api.port.EventBus;
import com.github.maximtereshchenko.clerk.write.api.port.Files;
import com.github.maximtereshchenko.clerk.write.api.port.TemplateEngine;
import com.github.maximtereshchenko.clerk.write.api.port.Templates;

import java.time.Clock;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public final class ClerkWriteFacade implements ClerkWriteModule {

    private final TemplateService templateService;
    private final DocumentService documentService;

    private ClerkWriteFacade(TemplateService templateService, DocumentService documentService) {
        this.templateService = templateService;
        this.documentService = documentService;
    }


    @Override
    public void createTemplate(UUID id, UUID fileId, String name)
            throws CouldNotExtendTimeToLive,
            CouldNotFindPlaceholders,
            TemplateIsEmpty,
            NameIsRequired,
            TemplateWithIdExists {
        templateService.createTemplate(id, fileId, name);
    }

    @Override
    public void createDocument(UUID id, UUID templateId, Map<String, String> values)
            throws CouldNotFindTemplate, ValuesAreRequired {
        documentService.createDocument(id, templateId, values);
    }

    public static final class Builder {

        private Files files;
        private TemplateEngine templateEngine;
        private Templates templates;
        private EventBus eventBus;
        private Clock clock;

        public Builder withFiles(Files files) {
            this.files = files;
            return this;
        }

        public Builder withTemplateEngine(TemplateEngine templateEngine) {
            this.templateEngine = templateEngine;
            return this;
        }

        public Builder withTemplates(Templates templates) {
            this.templates = templates;
            return this;
        }

        public Builder withEventBus(EventBus eventBus) {
            this.eventBus = eventBus;
            return this;
        }

        public Builder withClock(Clock clock) {
            this.clock = clock;
            return this;
        }

        public ClerkWriteModule build() {
            return new ClerkWriteFacade(
                    new TemplateService(
                            Objects.requireNonNull(files),
                            Objects.requireNonNull(templateEngine),
                            Objects.requireNonNull(templates),
                            Objects.requireNonNull(eventBus),
                            Objects.requireNonNull(clock)
                    ),
                    new DocumentService(
                            Objects.requireNonNull(templates),
                            Objects.requireNonNull(files),
                            templateEngine,
                            clock
                    )
            );
        }
    }
}
