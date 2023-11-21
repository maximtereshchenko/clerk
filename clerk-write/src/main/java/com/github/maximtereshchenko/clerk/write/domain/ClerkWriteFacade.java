package com.github.maximtereshchenko.clerk.write.domain;

import com.github.maximtereshchenko.clerk.write.api.ClerkWriteModule;
import com.github.maximtereshchenko.clerk.write.api.exception.CouldNotExtendTimeToLive;
import com.github.maximtereshchenko.clerk.write.api.exception.CouldNotFindPlaceholders;
import com.github.maximtereshchenko.clerk.write.api.port.EventStore;
import com.github.maximtereshchenko.clerk.write.api.port.Files;
import com.github.maximtereshchenko.clerk.write.api.port.TemplateEngine;
import java.time.Clock;
import java.util.UUID;

public final class ClerkWriteFacade implements ClerkWriteModule {

    private final TemplateService templateService;

    public ClerkWriteFacade(Files files, TemplateEngine templateEngine, EventStore eventStore, Clock clock) {
        templateService = new TemplateService(files, templateEngine, eventStore, clock);
    }

    @Override
    public void createTemplate(UUID id, UUID fileId, String name)
        throws CouldNotExtendTimeToLive, CouldNotFindPlaceholders {
        templateService.createTemplate(id, fileId, name);
    }
}
