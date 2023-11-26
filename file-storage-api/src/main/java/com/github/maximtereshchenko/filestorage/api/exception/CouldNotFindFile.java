package com.github.maximtereshchenko.filestorage.api.exception;

import java.util.UUID;

public final class CouldNotFindFile extends Exception {

    public CouldNotFindFile(UUID id) {
        super(id.toString());
    }
}
