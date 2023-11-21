package com.github.xini1.clerk.write.api.port;

import java.util.UUID;

public final class CouldNotFindFile extends Exception {

    public CouldNotFindFile(UUID id) {
        super(id.toString());
    }
}
