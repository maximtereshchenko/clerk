package com.github.maximtereshchenko.clerk.write.domain;

import com.github.maximtereshchenko.clerk.write.api.CreateTemplateUseCase;
import com.github.maximtereshchenko.clerk.write.api.exception.CouldNotExtendTimeToLive;
import com.github.maximtereshchenko.clerk.write.api.exception.CouldNotFindPlaceholders;
import com.github.maximtereshchenko.clerk.write.api.exception.TemplateIsEmpty;
import com.github.maximtereshchenko.clerk.write.api.port.EventStore;
import com.github.maximtereshchenko.clerk.write.api.port.Files;
import com.github.maximtereshchenko.clerk.write.api.port.TemplateEngine;
import com.github.maximtereshchenko.clerk.write.api.port.event.TemplateCreated;
import com.github.maximtereshchenko.clerk.write.api.port.exception.CouldNotFindFile;
import com.github.maximtereshchenko.clerk.write.api.port.exception.CouldNotReadInputStream;
import java.io.IOException;
import java.time.Clock;
import java.time.Instant;
import java.util.Set;
import java.util.UUID;

final class TemplateService implements CreateTemplateUseCase {

    private final Files files;
    private final TemplateEngine templateEngine;
    private final EventStore eventStore;
    private final Clock clock;

    TemplateService(Files files, TemplateEngine templateEngine, EventStore eventStore, Clock clock) {
        this.files = files;
        this.templateEngine = templateEngine;
        this.eventStore = eventStore;
        this.clock = clock;
    }

    @Override
    public void createTemplate(UUID id, UUID fileId, String name)
        throws CouldNotExtendTimeToLive, CouldNotFindPlaceholders, TemplateIsEmpty {
        setTimeToLive(id, fileId);
        var placeholders = placeholders(id, fileId);
        if (placeholders.isEmpty()) {
            throw new TemplateIsEmpty(id, fileId);
        }
        persistEvent(id, name, placeholders);
    }

    private void persistEvent(UUID id, String name, Set<String> placeholders) {
        eventStore.persist(
            new TemplateCreated(
                id,
                name,
                placeholders,
                1,
                clock.instant()
            )
        );
    }

    private Set<String> placeholders(UUID id, UUID fileId) throws CouldNotFindPlaceholders {
        try (var inputStream = files.inputStream(fileId)) {
            return templateEngine.placeholders(inputStream);
        } catch (CouldNotReadInputStream | IOException e) {
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
