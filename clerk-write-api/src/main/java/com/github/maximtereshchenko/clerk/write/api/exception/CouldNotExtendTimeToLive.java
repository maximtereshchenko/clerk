package com.github.maximtereshchenko.clerk.write.api.exception;

import java.util.UUID;

public final class CouldNotExtendTimeToLive extends Exception {

    public CouldNotExtendTimeToLive(UUID id, Throwable cause) {
        super(id.toString(), cause);
    }
}
