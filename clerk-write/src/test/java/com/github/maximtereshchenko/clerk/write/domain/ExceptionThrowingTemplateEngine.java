package com.github.maximtereshchenko.clerk.write.domain;

import com.github.maximtereshchenko.clerk.write.api.port.TemplateEngine;
import com.github.maximtereshchenko.clerk.write.api.port.exception.CouldNotReadInputStream;

import java.io.IOException;
import java.io.InputStream;
import java.util.Set;

final class ExceptionThrowingTemplateEngine implements TemplateEngine {

    @Override
    public Set<String> placeholders(InputStream inputStream) throws CouldNotReadInputStream {
        throw new CouldNotReadInputStream(new IOException());
    }
}
