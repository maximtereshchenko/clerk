package com.github.maximtereshchenko.clerk.write.domain;

import com.github.maximtereshchenko.clerk.event.TemplateCreated;
import com.github.maximtereshchenko.clerk.write.api.CreateTemplateUseCase;
import com.github.maximtereshchenko.clerk.write.api.exception.IdIsTaken;
import com.github.maximtereshchenko.clerk.write.api.exception.NameIsRequired;
import com.github.maximtereshchenko.clerk.write.api.exception.TemplateIsEmpty;
import com.github.maximtereshchenko.clerk.write.api.port.*;
import com.github.maximtereshchenko.clerk.write.api.port.exception.CouldNotFindFile;
import com.github.maximtereshchenko.clerk.write.api.port.exception.CouldNotProcessFile;
import com.github.maximtereshchenko.clerk.write.api.port.exception.FileIsExpired;

import java.io.IOException;
import java.time.Clock;
import java.time.Instant;
import java.util.Set;
import java.util.UUID;

final class TemplateService implements CreateTemplateUseCase {

    private final Files files;
    private final TemplateEngine templateEngine;
    private final Templates templates;
    private final EventBus eventBus;
    private final Clock clock;

    TemplateService(Files files, TemplateEngine templateEngine, Templates templates, EventBus eventBus, Clock clock) {
        this.files = files;
        this.templateEngine = templateEngine;
        this.templates = templates;
        this.eventBus = eventBus;
        this.clock = clock;
    }

    @Override
    public void createTemplate(UUID id, UUID userId, UUID fileId, String name)
            throws IOException,
            NameIsRequired,
            IdIsTaken,
            FileIsExpired,
            CouldNotFindFile,
            TemplateIsEmpty,
            CouldNotProcessFile {
        if (name.isBlank()) {
            throw new NameIsRequired(id);
        }
        if (templates.exists(id)) {
            throw new IdIsTaken(id);
        }
        files.setTimeToLive(fileId, userId, Instant.MAX);
        var placeholders = placeholders(fileId, userId);
        if (placeholders.isEmpty()) {
            throw new TemplateIsEmpty(id, fileId);
        }
        templates.persist(new PersistentTemplate(id, userId, fileId, name, placeholders));
        eventBus.publish(new TemplateCreated(id, userId, name, placeholders, clock.instant()));
    }

    private Set<String> placeholders(UUID fileId, UUID userId)
            throws IOException, CouldNotFindFile, FileIsExpired, CouldNotProcessFile {
        try (var inputStream = files.inputStream(fileId, userId)) {
            return templateEngine.placeholders(inputStream);
        }
    }
}
