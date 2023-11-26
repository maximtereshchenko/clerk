package com.github.maximtereshchenko.filestorage.domain;

import com.github.maximtereshchenko.filestorage.api.FileStorageModule;
import com.github.maximtereshchenko.filestorage.api.exception.IdIsUsed;
import com.github.maximtereshchenko.filestorage.api.port.FileLabel;
import com.github.maximtereshchenko.filestorage.api.port.FileLabels;
import com.github.maximtereshchenko.filestorage.api.port.Files;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UncheckedIOException;
import java.time.Instant;
import java.util.UUID;

public final class FileStorageFacade implements FileStorageModule {

    private final Files files;
    private final FileLabels fileLabels;

    public FileStorageFacade(Files files, FileLabels fileLabels) {
        this.files = files;
        this.fileLabels = fileLabels;
    }

    @Override
    public void persistFile(UUID id, Instant timeToLive, InputStream inputStream) throws IdIsUsed {
        if (fileLabels.exists(id)) {
            throw new IdIsUsed(id);
        }
        try (var outputStream = files.outputStream(id)) {
            inputStream.transferTo(outputStream);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
        fileLabels.persist(new FileLabel(id, timeToLive));
    }

    @Override
    public void writeFile(UUID id, OutputStream outputStream) {
        try (var inputStream = files.inputStream(id)) {
            inputStream.transferTo(outputStream);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
