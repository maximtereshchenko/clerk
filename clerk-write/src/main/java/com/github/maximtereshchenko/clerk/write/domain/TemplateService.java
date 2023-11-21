package com.github.maximtereshchenko.clerk.write.domain;

import com.github.maximtereshchenko.clerk.write.api.CreateTemplateUseCase;
import com.github.maximtereshchenko.clerk.write.api.exception.*;
import com.github.maximtereshchenko.clerk.write.api.port.*;
import com.github.maximtereshchenko.clerk.write.api.port.event.TemplateCreated;
import com.github.maximtereshchenko.clerk.write.api.port.exception.CouldNotFindFile;

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
    public void createTemplate(UUID id, UUID fileId, String name)
            throws CouldNotExtendTimeToLive,
            CouldNotFindPlaceholders,
            TemplateIsEmpty,
            NameIsRequired,
            TemplateWithIdExists {
        if (name.isBlank()) {
            throw new NameIsRequired(id);
        }
        if (templates.exists(id)) {
            throw new TemplateWithIdExists(id);
        }
        setTimeToLive(id, fileId);
        var placeholders = placeholders(id, fileId);
        if (placeholders.isEmpty()) {
            throw new TemplateIsEmpty(id, fileId);
        }
        persist(id, fileId, name, placeholders);
        publishIntegrationEvent(id, name, placeholders);
    }

    private void publishIntegrationEvent(UUID id, String name, Set<String> placeholders) {
        eventBus.publish(
                new TemplateCreated(
                        id,
                        name,
                        placeholders,
                        clock.instant()
                )
        );
    }

    private void persist(UUID id, UUID fileId, String name, Set<String> placeholders) {
        templates.persist(
                new PersistentTemplate(
                        id,
                        fileId,
                        name,
                        placeholders
                )
        );
    }

    private Set<String> placeholders(UUID id, UUID fileId) throws CouldNotFindPlaceholders {
        try (var inputStream = files.inputStream(fileId)) {
            return templateEngine.placeholders(inputStream);
        } catch (CouldNotFindFile | IOException e) {
            throw new CouldNotFindPlaceholders(id, fileId, e);
        }
    }

    private void setTimeToLive(UUID id, UUID fileId) throws CouldNotExtendTimeToLive {
        try {
            files.setTimeToLive(fileId, Instant.MAX);
        } catch (CouldNotFindFile e) {
            throw new CouldNotExtendTimeToLive(id, fileId, e);
        }
    }
}
