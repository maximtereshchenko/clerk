package com.github.maximtereshchenko.clerk.write.domain;

import com.github.maximtereshchenko.clerk.write.api.ClerkWriteModule;
import com.github.maximtereshchenko.clerk.write.domain.ClerkWriteFacade.Builder;
import com.github.maximtereshchenko.eventstore.inmemory.EventStoreInMemory;
import com.github.maximtereshchenko.files.inmemory.FilesInMemory;
import com.github.maximtereshchenko.templateengine.freemarker.TemplateEngineFreemarker;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.Map;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolver;

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

        private Context(FilesInMemory files, EventStoreInMemory eventStore, Clock clock, Builder builder) {
            this(
                Map.of(
                    FilesInMemory.class, files,
                    EventStoreInMemory.class, eventStore,
                    Clock.class, clock,
                    Builder.class, builder,
                    ClerkWriteModule.class, builder.build()
                )
            );
        }

        static Context create() {
            var files = new FilesInMemory();
            var eventStore = new EventStoreInMemory();
            var clock = Clock.fixed(Instant.parse("2020-01-01T00:00:00Z"), ZoneOffset.UTC);
            var builder = new Builder()
                .withFiles(files)
                .withEventStore(eventStore)
                .withClock(clock)
                .withTemplateEngine(TemplateEngineFreemarker.createDefault());
            return new Context(files, eventStore, clock, builder);
        }

        boolean supports(Class<?> type) {
            return parameters.containsKey(type);
        }

        Object parameter(Class<?> type) {
            return parameters.get(type);
        }
    }
}
