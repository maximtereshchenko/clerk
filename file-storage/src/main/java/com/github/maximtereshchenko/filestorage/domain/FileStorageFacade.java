package com.github.maximtereshchenko.filestorage.domain;

import com.github.maximtereshchenko.filestorage.api.FileStorageModule;
import com.github.maximtereshchenko.filestorage.api.exception.CouldNotFindFile;
import com.github.maximtereshchenko.filestorage.api.exception.FileBelongsToAnotherUser;
import com.github.maximtereshchenko.filestorage.api.exception.FileIsExpired;
import com.github.maximtereshchenko.filestorage.api.exception.IdIsUsed;
import com.github.maximtereshchenko.filestorage.api.port.FileLabel;
import com.github.maximtereshchenko.filestorage.api.port.FileLabels;
import com.github.maximtereshchenko.filestorage.api.port.Files;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.time.Clock;
import java.time.Instant;
import java.util.UUID;

public final class FileStorageFacade implements FileStorageModule {

    private final Files files;
    private final FileLabels fileLabels;
    private final Clock clock;

    public FileStorageFacade(Files files, FileLabels fileLabels, Clock clock) {
        this.files = files;
        this.fileLabels = fileLabels;
        this.clock = clock;
    }

    @Override
    public void persistFile(UUID id, UUID userId, Instant timeToLive, InputStream inputStream)
            throws IdIsUsed, IOException {
        if (fileLabels.exists(id)) {
            throw new IdIsUsed(id);
        }
        try (var outputStream = files.outputStream(id)) {
            inputStream.transferTo(outputStream);
        }
        fileLabels.persist(new FileLabel(id, userId, timeToLive));
    }

    @Override
    public void writeFile(UUID id, UUID userId, OutputStream outputStream)
            throws CouldNotFindFile, FileIsExpired, IOException, FileBelongsToAnotherUser {
        var fileLabel = fileLabel(id, userId);
        try (var inputStream = files.inputStream(fileLabel.id())) {
            inputStream.transferTo(outputStream);
        }
    }

    @Override
    public void setTimeToLive(UUID id, UUID userId, Instant timeToLive)
            throws CouldNotFindFile, FileIsExpired, FileBelongsToAnotherUser {
        var fileLabel = fileLabel(id, userId);
        fileLabels.update(new FileLabel(fileLabel.id(), fileLabel.userId(), timeToLive));
    }

    @Override
    public void cleanUp() throws IOException {
        for (var id : files.findAll()) {
            if (shouldBeRemoved(id)) {
                files.remove(id);
            }
        }
    }

    private FileLabel fileLabel(UUID id, UUID userId) throws CouldNotFindFile, FileIsExpired, FileBelongsToAnotherUser {
        var fileLabel = fileLabels.findById(id).orElseThrow(() -> new CouldNotFindFile(id));
        if (isExpired(fileLabel)) {
            throw new FileIsExpired(id, fileLabel.timeToLive());
        }
        if (!fileLabel.userId().equals(userId)) {
            throw new FileBelongsToAnotherUser(id, fileLabel.userId());
        }
        return fileLabel;
    }

    private boolean isExpired(FileLabel fileLabel) {
        return fileLabel.timeToLive().isBefore(clock.instant());
    }

    private boolean shouldBeRemoved(UUID id) {
        return fileLabels.findById(id)
                .map(this::isExpired)
                .orElse(Boolean.TRUE);
    }
}
