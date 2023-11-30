package com.github.maximtereshchenko.filestorage.api.exception;

import java.util.UUID;

public final class FileBelongsToAnotherUser extends Exception {

    public FileBelongsToAnotherUser(UUID id, UUID ownerId) {
        super("File ID: %s, owner ID: %s".formatted(id, ownerId));
    }
}
