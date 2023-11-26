package com.github.maximtereshchenko.filestorage.domain;

import com.github.maximtereshchenko.filestorage.api.FileStorageModule;
import com.github.maximtereshchenko.filestorage.api.exception.IdIsUsed;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.util.UUID;

abstract class UseCaseTest {

    void persistFile(FileStorageModule module, UUID id, Instant timeToLive, Path path) throws IOException, IdIsUsed {
        try (var inputStream = Files.newInputStream(path)) {
            module.persistFile(id, timeToLive, inputStream);
        }
    }
}
