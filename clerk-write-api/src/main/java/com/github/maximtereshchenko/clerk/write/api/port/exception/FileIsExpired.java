package com.github.maximtereshchenko.clerk.write.api.port.exception;

import java.util.UUID;

public final class FileIsExpired extends Exception {

    public FileIsExpired(UUID id) {
        super(id.toString());
    }
}
