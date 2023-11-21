package com.github.maximtereshchenko.clerk.write.api.port.exception;

import java.util.UUID;

public final class CouldNotFindFile extends Exception {

    public CouldNotFindFile(UUID id) {
        super(id.toString());
    }
}
