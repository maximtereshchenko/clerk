package com.github.maximtereshchenko.clerk.write.api.port;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;
import java.util.Set;

public interface TemplateEngine {

    Set<String> placeholders(InputStream inputStream);

    void fill(InputStream inputStream, Map<String, String> values, OutputStream outputStream);
}
