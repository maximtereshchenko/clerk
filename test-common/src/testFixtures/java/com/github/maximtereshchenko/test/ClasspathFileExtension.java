package com.github.maximtereshchenko.test;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

final class ClasspathFileExtension implements ParameterResolver {

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) {
        return parameterContext.getParameter().getType() == Path.class &&
               parameterContext.isAnnotated(ClasspathResource.class);
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext)
            throws ParameterResolutionException {
        return Paths.get(
                uri(
                        parameterContext.findAnnotation(ClasspathResource.class)
                                .map(ClasspathResource::value)
                                .filter(name -> !name.isBlank())
                                .orElse(parameterContext.getParameter().getName())
                )
        );
    }

    private URI uri(String name) {
        try {
            return url(name).toURI();
        } catch (URISyntaxException e) {
            throw new ParameterResolutionException("Could not create Path object", e);
        }
    }

    private URL url(String name) {
        var resource = Thread.currentThread()
                .getContextClassLoader()
                .getResource(name);
        if (resource == null) {
            throw new ParameterResolutionException("Could not find resource '%s' in classpath".formatted(name));
        }
        return resource;
    }
}
