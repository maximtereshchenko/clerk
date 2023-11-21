package com.github.maximtereshchenko.clerk.write.domain;

import com.github.maximtereshchenko.clerk.write.api.port.Files;

import java.nio.file.Path;
import java.time.Instant;
import java.util.UUID;

abstract class UseCaseTest {

    void persistFile(Files files, UUID fileId, Path template) {
        files.persist(
                fileId,
                Instant.MIN,
                outputStream -> outputStream.write(java.nio.file.Files.readAllBytes(template))
        );
    }
}
