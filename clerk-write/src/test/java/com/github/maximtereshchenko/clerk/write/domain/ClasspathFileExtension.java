package com.github.maximtereshchenko.clerk.write.domain;

import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;

final class ClasspathFileExtension implements ParameterResolver {

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) {
        return parameterContext.getParameter().getType() == Path.class;
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext)
        throws ParameterResolutionException {
        var name = parameterContext.getParameter().getName();
        var resource = Thread.currentThread()
            .getContextClassLoader()
            .getResource(name);
        if (resource == null) {
            throw new ParameterResolutionException("Could not find file '%s' in classpath".formatted(name));
        }
        try {
            return Paths.get(resource.toURI());
        } catch (URISyntaxException e) {
            throw new ParameterResolutionException("Could not create Path object", e);
        }
    }
}
