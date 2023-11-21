package com.github.maximtereshchenko.clerk.write.api.exception;

import java.util.UUID;

public final class CouldNotFindPlaceholders extends Exception {

    public CouldNotFindPlaceholders(UUID id, Throwable cause) {
        super(id.toString(), cause);
    }
}
