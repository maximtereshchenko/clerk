package com.github.maximtereshchenko.clerk.write.domain;

import com.github.maximtereshchenko.clerk.write.api.ClerkWriteModule;
import com.github.maximtereshchenko.clerk.write.api.exception.CouldNotExtendTimeToLive;
import com.github.maximtereshchenko.clerk.write.api.exception.CouldNotFindPlaceholders;
import com.github.maximtereshchenko.clerk.write.api.exception.TemplateIsEmpty;
import com.github.maximtereshchenko.clerk.write.api.port.EventBus;
import com.github.maximtereshchenko.clerk.write.api.port.EventStore;
import com.github.maximtereshchenko.clerk.write.api.port.Files;
import com.github.maximtereshchenko.clerk.write.api.port.TemplateEngine;

import java.time.Clock;
import java.util.Objects;
import java.util.UUID;

public final class ClerkWriteFacade implements ClerkWriteModule {

    private final TemplateService templateService;

    public ClerkWriteFacade(
            Files files,
            TemplateEngine templateEngine,
            EventStore eventStore,
            EventBus eventBus,
            Clock clock
    ) {
        templateService = new TemplateService(files, templateEngine, eventStore, eventBus, clock);
    }

    @Override
    public void createTemplate(UUID id, UUID fileId, String name)
            throws CouldNotExtendTimeToLive, CouldNotFindPlaceholders, TemplateIsEmpty {
        templateService.createTemplate(id, fileId, name);
    }

    public static final class Builder {

        private Files files;
        private TemplateEngine templateEngine;
        private EventStore eventStore;
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

        public Builder withEventStore(EventStore eventStore) {
            this.eventStore = eventStore;
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
                    Objects.requireNonNull(files),
                    Objects.requireNonNull(templateEngine),
                    Objects.requireNonNull(eventStore),
                    Objects.requireNonNull(eventBus),
                    Objects.requireNonNull(clock)
            );
        }
    }
}
