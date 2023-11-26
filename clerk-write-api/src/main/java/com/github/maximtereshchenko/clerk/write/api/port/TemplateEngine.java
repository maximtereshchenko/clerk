package com.github.maximtereshchenko.clerk.write.api.port;

import com.github.maximtereshchenko.clerk.write.api.port.exception.CouldNotProcessFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Set;

public interface TemplateEngine {

    Set<String> placeholders(InputStream inputStream) throws IOException, CouldNotProcessFile;

    byte[] fill(InputStream inputStream, Map<String, String> values) throws IOException, CouldNotProcessFile;
}
