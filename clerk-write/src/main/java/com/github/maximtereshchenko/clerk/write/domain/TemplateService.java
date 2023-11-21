package com.github.maximtereshchenko.clerk.write.domain;

import com.github.maximtereshchenko.clerk.write.api.CreateTemplateUseCase;
import com.github.maximtereshchenko.clerk.write.api.exception.CouldNotExtendTimeToLive;
import com.github.maximtereshchenko.clerk.write.api.port.CouldNotFindFile;
import com.github.maximtereshchenko.clerk.write.api.port.EventStore;
import com.github.maximtereshchenko.clerk.write.api.port.Files;
import com.github.maximtereshchenko.clerk.write.api.port.TemplateEngine;
import com.github.maximtereshchenko.clerk.write.api.port.event.TemplateCreated;
import java.time.Clock;
import java.time.Instant;
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
    public void createTemplate(UUID id, UUID fileId, String name) throws CouldNotExtendTimeToLive {
        setTimeToLive(id, fileId);
        eventStore.persist(
            new TemplateCreated(
                id,
                name,
                templateEngine.placeholders(files.inputStream(fileId)),
                1,
                clock.instant()
            )
        );
    }

    private void setTimeToLive(UUID id, UUID fileId) throws CouldNotExtendTimeToLive {
        try {
            files.setTimeToLive(fileId, Instant.MAX);
        } catch (CouldNotFindFile e) {
            throw new CouldNotExtendTimeToLive(id, e);
        }
    }
}
