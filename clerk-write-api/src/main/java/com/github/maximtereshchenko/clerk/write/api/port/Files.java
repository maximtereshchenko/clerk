package com.github.maximtereshchenko.clerk.write.api.port;

import com.github.maximtereshchenko.clerk.write.api.port.exception.CouldNotFindFile;
import com.github.maximtereshchenko.clerk.write.api.port.exception.FileIdIsTaken;
import com.github.maximtereshchenko.clerk.write.api.port.exception.FileIsExpired;

import java.io.IOException;
import java.io.InputStream;
import java.time.Instant;
import java.util.UUID;

public interface Files {

    void setTimeToLive(UUID id, UUID userId, Instant timeToLive) throws CouldNotFindFile, FileIsExpired;

    InputStream inputStream(UUID id, UUID userId) throws IOException, CouldNotFindFile, FileIsExpired;

    void persist(UUID id, UUID userId, Instant timeToLive, byte[] bytes) throws IOException, FileIdIsTaken;
}
