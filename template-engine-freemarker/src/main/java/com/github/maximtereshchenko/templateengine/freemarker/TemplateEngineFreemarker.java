package com.github.maximtereshchenko.templateengine.freemarker;

import com.github.maximtereshchenko.clerk.write.api.port.TemplateEngine;
import com.github.maximtereshchenko.clerk.write.api.port.exception.CouldNotReadInputStream;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.Set;

public final class TemplateEngineFreemarker implements TemplateEngine {

    private final Configuration configuration;

    public TemplateEngineFreemarker(Configuration configuration) {
        this.configuration = configuration;
    }

    public static TemplateEngine createDefault() {
        var configuration = new Configuration(Configuration.VERSION_2_3_32);
        configuration.setDefaultEncoding(StandardCharsets.UTF_8.displayName());
        configuration.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        configuration.setWrapUncheckedExceptions(true);
        return new TemplateEngineFreemarker(configuration);
    }

    @Override
    public Set<String> placeholders(InputStream inputStream) throws CouldNotReadInputStream {
        try {
            var template = new Template("", new InputStreamReader(inputStream), configuration);
            var placeholders = new Placeholders();
            template.process(placeholders, Writer.nullWriter());
            return placeholders.collected();
        } catch (IOException | TemplateException e) {
            throw new CouldNotReadInputStream(e);
        }
    }
}
