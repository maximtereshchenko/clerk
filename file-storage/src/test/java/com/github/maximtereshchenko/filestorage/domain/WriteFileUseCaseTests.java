package com.github.maximtereshchenko.filestorage.domain;

import com.github.maximtereshchenko.filestorage.api.FileStorageModule;
import com.github.maximtereshchenko.filestorage.api.exception.CouldNotFindFile;
import com.github.maximtereshchenko.filestorage.api.exception.FileIsExpired;
import com.github.maximtereshchenko.test.ClasspathFileExtension;
import com.github.maximtereshchenko.test.PredictableUUIDExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.OutputStream;
import java.nio.file.Path;
import java.time.Instant;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith({ClasspathFileExtension.class, PredictableUUIDExtension.class, FileStorageModuleExtension.class})
final class WriteFileUseCaseTests extends UseCaseTest {

    @Test
    void givenFileDoNotExist_whenWriteFile_thenCouldNotFindFileThrown(UUID id, FileStorageModule module) {
        var outputStream = OutputStream.nullOutputStream();

        assertThatThrownBy(() -> module.writeFile(id, outputStream))
                .isInstanceOf(CouldNotFindFile.class)
                .hasMessageContaining(id.toString());
    }

    @Test
    void givenExpiredFile_whenWriteFile_thenFileIsExpiredThrown(Path file, UUID id, FileStorageModule module)
            throws Exception {
        persistFile(module, id, Instant.MIN, file);
        var outputStream = OutputStream.nullOutputStream();

        assertThatThrownBy(() -> module.writeFile(id, outputStream))
                .isInstanceOf(FileIsExpired.class)
                .hasMessageContaining(id.toString())
                .hasMessageContaining(Instant.MIN.toString());
    }
}