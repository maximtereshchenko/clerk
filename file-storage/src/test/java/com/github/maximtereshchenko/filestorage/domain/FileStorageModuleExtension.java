package com.github.maximtereshchenko.filestorage.domain;

import com.github.maximtereshchenko.filestorage.api.FileStorageModule;
import com.github.maximtereshchenko.filestorage.filelabels.inmemory.FileLabelsInMemory;
import com.github.maximtereshchenko.filestorage.files.ondisk.FilesOnDisk;
import com.github.maximtereshchenko.test.TestContext;
import com.github.maximtereshchenko.test.TestContextExtension;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
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
                                    new FileLabelsInMemory()
                            )
                    )
            );
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
