package com.github.maximtereshchenko.clerk.write.domain;

import com.github.maximtereshchenko.clerk.write.api.ClerkWriteModule;
import com.github.maximtereshchenko.clerk.write.api.port.Files;
import com.github.maximtereshchenko.clerk.write.api.port.Templates;
import com.github.maximtereshchenko.clerk.write.domain.ClerkWriteFacade.Builder;
import com.github.maximtereshchenko.clerk.write.templateengine.freemarker.TemplateEngineFreemarker;
import com.github.maximtereshchenko.clerk.write.templates.inmemory.TemplatesInMemory;
import com.github.maximtereshchenko.test.TestContext;
import com.github.maximtereshchenko.test.TestContextExtension;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.Map;

final class ClerkWriteModuleExtension extends TestContextExtension {

    @Override
    protected TestContext testContext() {
        var files = new FilesInMemory();
        var templates = new TemplatesInMemory();
        var clock = Clock.fixed(Instant.parse("2020-01-01T00:00:00Z"), ZoneOffset.UTC);
        var eventBus = new EventBusInMemory();
        var builder = new Builder()
                .withFiles(files)
                .withTemplates(templates)
                .withClock(clock)
                .withTemplateEngine(TemplateEngineFreemarker.createDefault())
                .withEventBus(eventBus);
        return new TestContext(
                Map.of(
                        Files.class, files,
                        Templates.class, templates,
                        Clock.class, clock,
                        EventBusInMemory.class, eventBus,
                        Builder.class, builder,
                        ClerkWriteModule.class, builder.build()
                )
        );
    }
}
