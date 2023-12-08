package com.github.maximtereshchenko.test;

import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.testcontainers.containers.GenericContainer;

import java.util.Map;
import java.util.function.Function;

public abstract class ContainerExtension<T extends GenericContainer<?>> implements BeforeAllCallback, AfterAllCallback {

    private final ExtensionContext.Namespace namespace = ExtensionContext.Namespace.create(getClass());
    private final Map<String, Function<T, String>> propertyAccessors;

    protected ContainerExtension(Map<String, Function<T, String>> propertyAccessors) {
        this.propertyAccessors = Map.copyOf(propertyAccessors);
    }

    @Override
    public final void beforeAll(ExtensionContext context) {
        var container = container();
        container.start();
        propertyAccessors.forEach((key, accessor) -> System.setProperty(key, accessor.apply(container)));
        context.getStore(namespace).put(GenericContainer.class, container);
    }

    @Override
    public final void afterAll(ExtensionContext context) {
        context.getStore(namespace).get(GenericContainer.class, GenericContainer.class).stop();
        propertyAccessors.keySet().forEach(System::clearProperty);
    }

    protected abstract T container();
}
