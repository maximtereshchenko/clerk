package com.github.maximtereshchenko.filestorage.api.exception;

import java.time.Instant;
import java.util.UUID;

public final class FileIsExpired extends Exception {

    public FileIsExpired(UUID id, Instant timeToLive) {
        super("File %s is expired at %s".formatted(id, timeToLive));
    }
}
