package com.github.maximtereshchenko.filestorage.api;

import com.github.maximtereshchenko.filestorage.api.exception.CouldNotFindFile;
import com.github.maximtereshchenko.filestorage.api.exception.FileBelongsToAnotherUser;
import com.github.maximtereshchenko.filestorage.api.exception.FileIsExpired;

import java.time.Instant;
import java.util.UUID;

public interface SetTimeToLiveUseCase {

    void setTimeToLive(UUID id, UUID userId, Instant timeToLive)
            throws CouldNotFindFile, FileIsExpired, FileBelongsToAnotherUser;
}
