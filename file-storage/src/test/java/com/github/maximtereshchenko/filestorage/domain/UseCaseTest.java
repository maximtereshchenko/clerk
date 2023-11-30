package com.github.maximtereshchenko.filestorage.domain;

import com.github.maximtereshchenko.filestorage.api.PersistFileUseCase;
import com.github.maximtereshchenko.filestorage.api.exception.IdIsUsed;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.util.UUID;

abstract class UseCaseTest {

    void persistFile(PersistFileUseCase useCase, UUID id, UUID userId, Path path) throws IOException, IdIsUsed {
        persistFile(useCase, id, userId, Instant.MAX, path);
    }

    void persistFile(PersistFileUseCase useCase, UUID id, UUID userId, Instant timeToLive, Path path)
            throws IOException, IdIsUsed {
        try (var inputStream = Files.newInputStream(path)) {
            useCase.persistFile(id, userId, timeToLive, inputStream);
        }
    }
}
