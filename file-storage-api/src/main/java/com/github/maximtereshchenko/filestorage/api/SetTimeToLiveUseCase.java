package com.github.maximtereshchenko.filestorage.api;

import com.github.maximtereshchenko.filestorage.api.exception.CouldNotFindFile;
import com.github.maximtereshchenko.filestorage.api.exception.FileIsExpired;

import java.time.Instant;
import java.util.UUID;

public interface SetTimeToLiveUseCase {

    void setTimeToLive(UUID id, Instant timeToLive) throws CouldNotFindFile, FileIsExpired;
}
