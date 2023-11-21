package com.github.xini1.clerk.write.api.exception;

import java.util.UUID;

public final class CouldNotExtendTimeToLive extends Exception {

    public CouldNotExtendTimeToLive(UUID id, Throwable cause) {
        super(id.toString(), cause);
    }
}
