package com.github.maximtereshchenko.clerk.write.api.port;

import java.io.InputStream;
import java.time.Instant;
import java.util.UUID;

public interface Files {

    void setTimeToLive(UUID id, Instant timeToLive) throws CouldNotFindFile;

    InputStream inputStream(UUID id);
}