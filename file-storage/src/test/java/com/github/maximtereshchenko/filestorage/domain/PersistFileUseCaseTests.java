package com.github.maximtereshchenko.filestorage.domain;

import com.github.maximtereshchenko.filestorage.api.FileStorageModule;
import com.github.maximtereshchenko.filestorage.api.exception.IdIsUsed;
import com.github.maximtereshchenko.test.ClasspathFileExtension;
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

@ExtendWith({ClasspathFileExtension.class, PredictableUUIDExtension.class, FileStorageModuleExtension.class})
final class PersistFileUseCaseTests extends UseCaseTest {

    @Test
    void givenFile_whenPersistFile_thenFileCanBeDownloaded(Path file, UUID id, FileStorageModule module)
            throws Exception {
        persistFile(module, id, Instant.MAX, file);
        var outputStream = new ByteArrayOutputStream();

        module.writeFile(id, outputStream);

        assertThat(outputStream.toString(StandardCharsets.UTF_8)).isEqualTo(Files.readString(file));
    }

    @Test
    void givenIdIsUsed_whenPersistFile_thenIdIsUsedThrown(UUID id, FileStorageModule module) throws Exception {
        module.persistFile(id, Instant.MAX, InputStream.nullInputStream());
        var inputStream = InputStream.nullInputStream();

        assertThatThrownBy(() -> module.persistFile(id, Instant.MAX, inputStream))
                .isInstanceOf(IdIsUsed.class)
                .hasMessageContaining(id.toString());
    }
}