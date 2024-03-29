package com.github.maximtereshchenko.filestorage.api.port;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;

public interface Files {

    OutputStream outputStream(UUID id) throws IOException;

    InputStream inputStream(UUID id) throws IOException;

    Set<UUID> findAll() throws IOException;

    void remove(UUID id) throws IOException;
}
