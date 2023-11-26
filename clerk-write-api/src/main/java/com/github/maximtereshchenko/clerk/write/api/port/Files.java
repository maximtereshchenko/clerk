package com.github.maximtereshchenko.clerk.write.api.port;

import com.github.maximtereshchenko.clerk.write.api.port.exception.CouldNotFindFile;
import com.github.maximtereshchenko.clerk.write.api.port.exception.FileIdIsTaken;
import com.github.maximtereshchenko.clerk.write.api.port.exception.FileIsExpired;

import java.io.IOException;
import java.io.InputStream;
import java.time.Instant;
import java.util.UUID;

public interface Files {

    void setTimeToLive(UUID id, Instant timeToLive) throws CouldNotFindFile, FileIsExpired;

    InputStream inputStream(UUID id) throws IOException, CouldNotFindFile, FileIsExpired;

    void persist(UUID id, Instant timeToLive, byte[] bytes) throws IOException, FileIdIsTaken;
}
