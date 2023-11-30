package com.github.maximtereshchenko.clerk.write.domain;

import com.github.maximtereshchenko.clerk.event.DocumentCreated;
import com.github.maximtereshchenko.clerk.write.api.CreateDocumentFromTemplateUseCase;
import com.github.maximtereshchenko.clerk.write.api.exception.CouldNotFindTemplate;
import com.github.maximtereshchenko.clerk.write.api.exception.ValuesAreRequired;
import com.github.maximtereshchenko.clerk.write.api.port.EventBus;
import com.github.maximtereshchenko.clerk.write.api.port.Files;
import com.github.maximtereshchenko.clerk.write.api.port.TemplateEngine;
import com.github.maximtereshchenko.clerk.write.api.port.Templates;
import com.github.maximtereshchenko.clerk.write.api.port.exception.CouldNotFindFile;
import com.github.maximtereshchenko.clerk.write.api.port.exception.CouldNotProcessFile;
import com.github.maximtereshchenko.clerk.write.api.port.exception.FileIdIsTaken;
import com.github.maximtereshchenko.clerk.write.api.port.exception.FileIsExpired;

import java.io.IOException;
import java.time.Clock;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.UUID;

final class DocumentService implements CreateDocumentFromTemplateUseCase {

    private final Templates templates;
    private final Files files;
    private final TemplateEngine templateEngine;
    private final EventBus eventBus;
    private final Clock clock;

    DocumentService(Templates templates, Files files, TemplateEngine templateEngine, EventBus eventBus, Clock clock) {
        this.templates = templates;
        this.files = files;
        this.templateEngine = templateEngine;
        this.eventBus = eventBus;
        this.clock = clock;
    }

    @Override
    public void createDocument(UUID id, UUID userId, UUID templateId, Map<String, String> values)
            throws IOException,
            ValuesAreRequired,
            FileIdIsTaken,
            CouldNotFindTemplate,
            FileIsExpired,
            CouldNotFindFile,
            CouldNotProcessFile {
        if (values.isEmpty()) {
            throw new ValuesAreRequired(id);
        }
        var instant = clock.instant();
        var timeToLive = instant.plus(1, ChronoUnit.DAYS);
        files.persist(id, userId, timeToLive, document(templateFileId(id, templateId), userId, values));
        eventBus.publish(new DocumentCreated(id, userId, timeToLive, instant));
    }

    private byte[] document(UUID fileId, UUID userId, Map<String, String> values)
            throws IOException, FileIsExpired, CouldNotFindFile, CouldNotProcessFile {
        try (var inputStream = files.inputStream(fileId, userId)) {
            return templateEngine.fill(inputStream, values);
        }
    }

    private UUID templateFileId(UUID documentId, UUID templateId) throws CouldNotFindTemplate {
        return templates.findById(templateId)
                .orElseThrow(() -> new CouldNotFindTemplate(documentId, templateId))
                .fileId();
    }
}
