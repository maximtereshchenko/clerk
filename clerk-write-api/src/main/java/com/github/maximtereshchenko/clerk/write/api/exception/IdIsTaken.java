package com.github.maximtereshchenko.clerk.write.api.exception;

import java.util.UUID;

public final class IdIsTaken extends Exception {

    public IdIsTaken(UUID id) {
        super(id.toString());
    }
}
