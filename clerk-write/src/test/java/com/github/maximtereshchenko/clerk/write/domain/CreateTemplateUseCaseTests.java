package com.github.maximtereshchenko.clerk.write.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.github.maximtereshchenko.clerk.write.api.ClerkWriteModule;
import com.github.maximtereshchenko.clerk.write.api.exception.CouldNotExtendTimeToLive;
import com.github.maximtereshchenko.clerk.write.api.port.CouldNotFindFile;
import com.github.maximtereshchenko.files.inmemory.FilesInMemory;
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
    void givenFileExists_whenCreateTemplate_thenFileTimeToLiveExtended(UUID fileId, Path template, UUID templateId)
        throws Exception {
        files.save(fileId, template, Instant.MIN);

        module.createTemplate(templateId, fileId, "name");

        assertThat(files.timeToLive(fileId)).isEqualTo(Instant.MAX);
    }

    @Test
    void givenFileDoNotExist_whenCreateTemplate_thenCouldNotExtendTimeToLiveThrown(UUID fileId, UUID templateId) {
        assertThatThrownBy(() -> module.createTemplate(templateId, fileId, "name"))
            .isInstanceOf(CouldNotExtendTimeToLive.class)
            .hasMessage(templateId.toString())
            .hasCauseInstanceOf(CouldNotFindFile.class);
    }
}