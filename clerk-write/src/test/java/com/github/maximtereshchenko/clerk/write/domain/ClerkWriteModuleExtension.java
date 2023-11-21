package com.github.maximtereshchenko.clerk.write.domain;

import com.github.maximtereshchenko.clerk.write.api.ClerkWriteModule;
import com.github.maximtereshchenko.clerk.write.api.port.Files;
import com.github.maximtereshchenko.clerk.write.api.port.Templates;
import com.github.maximtereshchenko.clerk.write.domain.ClerkWriteFacade.Builder;
import com.github.maximtereshchenko.eventstore.inmemory.TemplatesInMemory;
import com.github.maximtereshchenko.files.inmemory.FilesInMemory;
import com.github.maximtereshchenko.templateengine.freemarker.TemplateEngineFreemarker;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolver;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.Map;

final class ClerkWriteModuleExtension implements ParameterResolver {

    private final ExtensionContext.Namespace namespace = ExtensionContext.Namespace.create(
            ClerkWriteModuleExtension.class);

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) {
        return extensionContext.getStore(namespace)
                .getOrComputeIfAbsent(extensionContext.getUniqueId(), id -> Context.create(), Context.class)
                .supports(parameterContext.getParameter().getType());
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) {
        return extensionContext.getStore(namespace)
                .get(extensionContext.getUniqueId(), Context.class)
                .parameter(parameterContext.getParameter().getType());
    }

    private record Context(Map<Class<?>, Object> parameters) {

        private Context(
                FilesInMemory files,
                Templates eventStore,
                Clock clock,
                EventBusInMemory eventBus,
                Builder builder
        ) {
            this(
                    Map.of(
                            Files.class, files,
                            FilesInMemory.class, files,
                            Templates.class, eventStore,
                            Clock.class, clock,
                            EventBusInMemory.class, eventBus,
                            Builder.class, builder,
                            ClerkWriteModule.class, builder.build()
                    )
            );
        }

        static Context create() {
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
            return new Context(files, templates, clock, eventBus, builder);
        }

        boolean supports(Class<?> type) {
            return parameters.containsKey(type);
        }

        Object parameter(Class<?> type) {
            return parameters.get(type);
        }
    }
}
