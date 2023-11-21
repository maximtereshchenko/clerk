package com.github.xini1.clerk.write.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.github.xini1.clerk.write.api.ClerkWriteModule;
import com.github.xini1.files.inmemory.FilesInMemory;
import java.nio.file.Path;
import java.time.Instant;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith({ClasspathFileExtension.class, PredictableUUIDExtension.class})
final class CreateTemplateUseCaseTests {

    private final FilesInMemory files = new FilesInMemory();
    private final ClerkWriteModule module = new ClerkWriteFacade(files);

    @Test
    void givenFileExists_whenCreateTemplate_thenFileTimeToLiveExtended(UUID fileId, Path template, UUID templateId) {
        files.save(fileId, template, Instant.MIN);

        module.createTemplate(templateId, fileId, "name");

        assertThat(files.timeToLive(fileId)).isEqualTo(Instant.MAX);
    }
}