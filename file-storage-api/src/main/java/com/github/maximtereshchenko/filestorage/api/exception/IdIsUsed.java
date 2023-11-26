package com.github.maximtereshchenko.filestorage.api.exception;

import java.util.UUID;

public final class IdIsUsed extends Exception {

    public IdIsUsed(UUID id) {
        super(id.toString());
    }
}
