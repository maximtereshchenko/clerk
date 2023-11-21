package com.github.xini1.clerk.write.api.port;

import java.util.UUID;

public final class FileNotFound extends Exception {

    public FileNotFound(UUID id) {
        super(id.toString());
    }
}
