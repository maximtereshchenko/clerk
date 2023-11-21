package com.github.maximtereshchenko.clerk.write.api.port;

import com.github.maximtereshchenko.clerk.write.api.port.exception.CouldNotFindFile;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.time.Instant;
import java.util.UUID;

public interface Files {

    void setTimeToLive(UUID id, Instant timeToLive) throws CouldNotFindFile;

    InputStream inputStream(UUID id) throws CouldNotFindFile;

    void persist(UUID id, Instant timeToLive, OutputStreamConsumer consumer);

    @FunctionalInterface
    interface OutputStreamConsumer {

        void accept(OutputStream outputStream) throws IOException;
    }
}
