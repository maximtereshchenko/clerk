package com.github.maximtereshchenko.filestorage.domain;

import com.github.maximtereshchenko.filestorage.api.FileStorageModule;
import com.github.maximtereshchenko.filestorage.api.exception.CouldNotFindFile;
import com.github.maximtereshchenko.test.ClasspathFileExtension;
import com.github.maximtereshchenko.test.PredictableUUIDExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.OutputStream;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith({ClasspathFileExtension.class, PredictableUUIDExtension.class, FileStorageModuleExtension.class})
final class WriteFileUseCaseTests {

    @Test
    void givenFileDoNotExist_whenWriteFile_thenCouldNotFindFileThrown(UUID id, FileStorageModule module) {
        var outputStream = OutputStream.nullOutputStream();

        assertThatThrownBy(() -> module.writeFile(id, outputStream))
                .isInstanceOf(CouldNotFindFile.class)
                .hasMessageContaining(id.toString());
    }
}