package com.github.maximtereshchenko.clerk.write.api.port.exception;

import java.util.UUID;

public final class FileIdIsTaken extends Exception {

    public FileIdIsTaken(UUID id) {
        super(id.toString());
    }
}
