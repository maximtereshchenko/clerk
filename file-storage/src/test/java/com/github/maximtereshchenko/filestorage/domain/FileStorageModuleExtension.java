package com.github.maximtereshchenko.filestorage.domain;

import com.github.maximtereshchenko.filestorage.api.FileStorageModule;
import com.github.maximtereshchenko.filestorage.filelabels.inmemory.FileLabelsInMemory;
import com.github.maximtereshchenko.filestorage.files.ondisk.FilesOnDisk;
import com.github.maximtereshchenko.test.TestContext;
import com.github.maximtereshchenko.test.TestContextExtension;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.Map;

final class FileStorageModuleExtension extends TestContextExtension {

    @Override
    protected TestContext testContext() {
        try {
            return new TestContext(
                    Map.of(
                            FileStorageModule.class,
                            new FileStorageFacade(
                                    new FilesOnDisk(Files.createTempDirectory(null)),
                                    new FileLabelsInMemory(),
                                    Clock.fixed(Instant.parse("2020-01-01T00:00:00Z"), ZoneOffset.UTC)
                            )
                    )
            );
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
