package com.github.maximtereshchenko.filestorage.api;

import com.github.maximtereshchenko.filestorage.api.exception.IdIsUsed;

import java.io.IOException;
import java.io.InputStream;
import java.time.Instant;
import java.util.UUID;

public interface PersistFileUseCase {

    void persistFile(UUID id, UUID userId, Instant timeToLive, InputStream inputStream) throws IdIsUsed, IOException;
}
