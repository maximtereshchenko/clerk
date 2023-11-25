package com.github.maximtereshchenko.test;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolver;

public abstract class TestContextExtension implements ParameterResolver {

    private final ExtensionContext.Namespace namespace = ExtensionContext.Namespace.create(getClass());

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) {
        return extensionContext.getStore(namespace)
                .getOrComputeIfAbsent(extensionContext.getUniqueId(), id -> testContext(), TestContext.class)
                .supports(parameterContext.getParameter().getType());
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) {
        return extensionContext.getStore(namespace)
                .get(extensionContext.getUniqueId(), TestContext.class)
                .parameter(parameterContext.getParameter().getType());
    }

    protected abstract TestContext testContext();
}
