package com.github.maximtereshchenko.clerk.write.domain;

import com.github.maximtereshchenko.clerk.write.api.CreateDocumentFromTemplateUseCase;
import com.github.maximtereshchenko.clerk.write.api.exception.CouldNotFindTemplate;
import com.github.maximtereshchenko.clerk.write.api.exception.UnexpectedException;
import com.github.maximtereshchenko.clerk.write.api.exception.ValuesAreRequired;
import com.github.maximtereshchenko.clerk.write.api.port.Files;
import com.github.maximtereshchenko.clerk.write.api.port.TemplateEngine;
import com.github.maximtereshchenko.clerk.write.api.port.Templates;
import com.github.maximtereshchenko.clerk.write.api.port.exception.CouldNotFindFile;

import java.io.InputStream;
import java.time.Clock;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.UUID;

final class DocumentService implements CreateDocumentFromTemplateUseCase {

    private final Templates templates;
    private final Files files;
    private final TemplateEngine templateEngine;
    private final Clock clock;

    DocumentService(Templates templates, Files files, TemplateEngine templateEngine, Clock clock) {
        this.templates = templates;
        this.files = files;
        this.templateEngine = templateEngine;
        this.clock = clock;
    }

    @Override
    public void createDocument(UUID id, UUID templateId, Map<String, String> values)
            throws CouldNotFindTemplate, ValuesAreRequired {
        if (values.isEmpty()) {
            throw new ValuesAreRequired(id);
        }
        var fileId = fileId(id, templateId);
        files.persist(
                id,
                clock.instant().plus(1, ChronoUnit.DAYS),
                outputStream -> templateEngine.fill(inputStream(fileId), values, outputStream)
        );
    }

    private UUID fileId(UUID documentId, UUID templateId) throws CouldNotFindTemplate {
        return templates.findById(templateId)
                .orElseThrow(() -> new CouldNotFindTemplate(documentId, templateId))
                .fileId();
    }

    private InputStream inputStream(UUID fileId) {
        try {
            return files.inputStream(fileId);
        } catch (CouldNotFindFile e) {
            throw new UnexpectedException("Expected existing file " + fileId, e);
        }
    }
}
