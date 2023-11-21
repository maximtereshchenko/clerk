package com.github.maximtereshchenko.clerk.write.api.port;

import java.io.InputStream;
import java.util.Set;

public interface TemplateEngine {

    Set<String> placeholders(InputStream inputStream);
}
