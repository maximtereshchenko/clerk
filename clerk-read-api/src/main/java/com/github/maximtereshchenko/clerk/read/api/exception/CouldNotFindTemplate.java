package com.github.maximtereshchenko.clerk.read.api.exception;

import java.util.UUID;

public final class CouldNotFindTemplate extends Exception {

    public CouldNotFindTemplate(UUID id) {
        super(id.toString());
    }
}
