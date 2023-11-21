package com.github.maximtereshchenko.clerk.write.api.port;

import com.github.maximtereshchenko.clerk.write.api.port.exception.CouldNotReadInputStream;
import java.io.InputStream;
import java.util.Set;

public interface TemplateEngine {

    Set<String> placeholders(InputStream inputStream) throws CouldNotReadInputStream;
}
