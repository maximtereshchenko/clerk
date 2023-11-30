package com.github.maximtereshchenko.filestorage.domain;

import com.github.maximtereshchenko.filestorage.api.FileStorageModule;
import com.github.maximtereshchenko.filestorage.api.exception.CouldNotFindFile;
import com.github.maximtereshchenko.filestorage.api.exception.FileIsExpired;
import com.github.maximtereshchenko.test.ClasspathFileExtension;
import com.github.maximtereshchenko.test.ManualClock;
import com.github.maximtereshchenko.test.PredictableUUIDExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith({ClasspathFileExtension.class, PredictableUUIDExtension.class, FileStorageModuleExtension.class})
final class SetTimeToLiveUseCaseTests extends UseCaseTest {

    @Test
    void givenFileExists_whenSetTimeToLive_thenFileIsNotCleaned(
            Path file,
            UUID id,
            UUID userId,
            ManualClock clock,
            FileStorageModule module
    ) throws Exception {
        persistFile(module, id, userId, clock.instant().plus(30, ChronoUnit.MINUTES), file);
        module.setTimeToLive(id, userId, clock.instant().plus(90, ChronoUnit.MINUTES));
        clock.waitOneHour();
        var outputStream = new ByteArrayOutputStream();

        module.cleanUp();
        module.writeFile(id, userId, outputStream);

        assertThat(outputStream.toString(StandardCharsets.UTF_8)).isEqualTo(Files.readString(file));
    }

    @Test
    void givenFileDoNotExist_whenSetTimeToLive_thenCouldNotFindFileThrown(
            UUID id,
            UUID userId,
            FileStorageModule module
    ) {
        assertThatThrownBy(() -> module.setTimeToLive(id, userId, Instant.MAX))
                .isInstanceOf(CouldNotFindFile.class)
                .hasMessageContaining(id.toString());
    }

    @Test
    void givenExpiredFile_whenSetTimeToLive_thenFileIsExpiredThrown(
            Path file,
            UUID id,
            UUID userId,
            FileStorageModule module
    ) throws Exception {
        persistFile(module, id, userId, Instant.MIN, file);

        assertThatThrownBy(() -> module.setTimeToLive(id, userId, Instant.MAX))
                .isInstanceOf(FileIsExpired.class)
                .hasMessageContaining(id.toString())
                .hasMessageContaining(Instant.MIN.toString());
    }
}