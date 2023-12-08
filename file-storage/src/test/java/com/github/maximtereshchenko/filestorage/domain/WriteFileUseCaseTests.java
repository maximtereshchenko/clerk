package com.github.maximtereshchenko.filestorage.domain;

import com.github.maximtereshchenko.filestorage.api.FileStorageModule;
import com.github.maximtereshchenko.filestorage.api.exception.CouldNotFindFile;
import com.github.maximtereshchenko.filestorage.api.exception.FileBelongsToAnotherUser;
import com.github.maximtereshchenko.filestorage.api.exception.FileIsExpired;
import com.github.maximtereshchenko.test.ClasspathResource;
import com.github.maximtereshchenko.test.PredictableUUIDExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.OutputStream;
import java.nio.file.Path;
import java.time.Instant;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith({PredictableUUIDExtension.class, FileStorageModuleExtension.class})
final class WriteFileUseCaseTests extends UseCaseTest {

    @Test
    void givenFileDoNotExist_whenWriteFile_thenCouldNotFindFileThrown(UUID id, UUID userId, FileStorageModule module) {
        var outputStream = OutputStream.nullOutputStream();

        assertThatThrownBy(() -> module.writeFile(id, userId, outputStream))
                .isInstanceOf(CouldNotFindFile.class)
                .hasMessageContaining(id.toString());
    }

    @Test
    void givenExpiredFile_whenWriteFile_thenFileIsExpiredThrown(
            @ClasspathResource Path file,
            UUID id,
            UUID userId,
            FileStorageModule module
    )
            throws Exception {
        persistFile(module, id, userId, Instant.MIN, file);
        var outputStream = OutputStream.nullOutputStream();

        assertThatThrownBy(() -> module.writeFile(id, userId, outputStream))
                .isInstanceOf(FileIsExpired.class)
                .hasMessageContaining(id.toString())
                .hasMessageContaining(Instant.MIN.toString());
    }

    @Test
    void givenFileBelongsToAnotherUser_whenWriteFile_thenFileBelongsToAnotherUserThrown(
            @ClasspathResource Path file,
            UUID id,
            UUID otherUserId,
            UUID userId,
            FileStorageModule module
    ) throws Exception {
        persistFile(module, id, otherUserId, file);
        var outputStream = OutputStream.nullOutputStream();

        assertThatThrownBy(() -> module.writeFile(id, userId, outputStream))
                .isInstanceOf(FileBelongsToAnotherUser.class)
                .hasMessageContaining(id.toString())
                .hasMessageContaining(otherUserId.toString());
    }
}