package com.github.maximtereshchenko.filestorage.domain;

import com.github.maximtereshchenko.filestorage.api.FileStorageModule;
import com.github.maximtereshchenko.filestorage.api.exception.IdIsUsed;
import com.github.maximtereshchenko.test.ClasspathResource;
import com.github.maximtereshchenko.test.PredictableUUIDExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith({PredictableUUIDExtension.class, FileStorageModuleExtension.class})
final class PersistFileUseCaseTests extends UseCaseTest {

    @Test
    void givenFile_whenPersistFile_thenFileCanBeDownloaded(
            @ClasspathResource Path file,
            UUID id,
            UUID userId,
            FileStorageModule module
    )
            throws Exception {
        persistFile(module, id, userId, file);
        var outputStream = new ByteArrayOutputStream();

        module.writeFile(id, userId, outputStream);

        assertThat(outputStream.toString(StandardCharsets.UTF_8)).isEqualTo(Files.readString(file));
    }

    @Test
    void givenIdIsUsed_whenPersistFile_thenIdIsUsedThrown(
            @ClasspathResource Path file,
            UUID id,
            UUID userId,
            FileStorageModule module
    )
            throws Exception {
        persistFile(module, id, userId, file);
        var inputStream = InputStream.nullInputStream();

        assertThatThrownBy(() -> module.persistFile(id, userId, Instant.MAX, inputStream))
                .isInstanceOf(IdIsUsed.class)
                .hasMessageContaining(id.toString());
    }
}