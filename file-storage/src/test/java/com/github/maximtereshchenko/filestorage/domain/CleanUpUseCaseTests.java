package com.github.maximtereshchenko.filestorage.domain;

import com.github.maximtereshchenko.filestorage.api.FileStorageModule;
import com.github.maximtereshchenko.filestorage.api.port.Files;
import com.github.maximtereshchenko.test.ClasspathFileExtension;
import com.github.maximtereshchenko.test.PredictableUUIDExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.IOException;
import java.nio.file.Path;
import java.time.Instant;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith({ClasspathFileExtension.class, PredictableUUIDExtension.class, FileStorageModuleExtension.class})
final class CleanUpUseCaseTests extends UseCaseTest {

    @Test
    void givenUnknownFile_whenCleanUp_thenFileIsCleaned(UUID id, Path file, Files files, FileStorageModule module)
            throws IOException {
        try (
                var inputStream = java.nio.file.Files.newInputStream(file);
                var outputStream = files.outputStream(id)
        ) {
            inputStream.transferTo(outputStream);
        }

        module.cleanUp();

        assertThatThrownBy(() -> files.inputStream(id))
                .isInstanceOf(IOException.class);
    }

    @Test
    void givenExpiredFile_whenCleanUp_thenFileIsCleaned(UUID id, Path file, Files files, FileStorageModule module)
            throws Exception {
        persistFile(module, id, Instant.MIN, file);

        module.cleanUp();

        assertThatThrownBy(() -> files.inputStream(id))
                .isInstanceOf(IOException.class);
    }
}
