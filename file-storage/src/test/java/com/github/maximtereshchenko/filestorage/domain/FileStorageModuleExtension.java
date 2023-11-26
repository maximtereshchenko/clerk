package com.github.maximtereshchenko.filestorage.domain;

import com.github.maximtereshchenko.filestorage.api.FileStorageModule;
import com.github.maximtereshchenko.filestorage.api.port.Files;
import com.github.maximtereshchenko.filestorage.filelabels.inmemory.FileLabelsInMemory;
import com.github.maximtereshchenko.filestorage.files.ondisk.FilesOnDisk;
import com.github.maximtereshchenko.test.ManualClock;
import com.github.maximtereshchenko.test.TestContext;
import com.github.maximtereshchenko.test.TestContextExtension;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Map;

final class FileStorageModuleExtension extends TestContextExtension {

    @Override
    protected TestContext testContext() {
        try {
            var files = new FilesOnDisk(java.nio.file.Files.createTempDirectory(null));
            var clock = new ManualClock();
            return new TestContext(
                    Map.of(
                            Files.class, files,
                            ManualClock.class, clock,
                            FileStorageModule.class, new FileStorageFacade(files, new FileLabelsInMemory(), clock)
                    )
            );
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
