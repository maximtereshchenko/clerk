package com.github.maximtereshchenko.filestorage.domain;

import com.github.maximtereshchenko.filestorage.api.FileStorageModule;
import com.github.maximtereshchenko.test.ClasspathFileExtension;
import com.github.maximtereshchenko.test.ManualClock;
import com.github.maximtereshchenko.test.PredictableUUIDExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith({ClasspathFileExtension.class, PredictableUUIDExtension.class, FileStorageModuleExtension.class})
final class SetTimeToLiveUseCaseTests extends UseCaseTest {

    @Test
    void givenFileExist_whenSetTimeToLive_thenFileIsNotCleaned(
            Path file,
            UUID id,
            ManualClock clock,
            FileStorageModule module
    ) throws Exception {
        persistFile(module, id, clock.instant().plus(30, ChronoUnit.MINUTES), file);
        module.setTimeToLive(id, clock.instant().plus(90, ChronoUnit.MINUTES));
        clock.waitOneHour();
        var outputStream = new ByteArrayOutputStream();

        module.cleanUp();
        module.writeFile(id, outputStream);

        assertThat(outputStream.toString(StandardCharsets.UTF_8)).isEqualTo(Files.readString(file));

    }
}