package com.github.maximtereshchenko.clerk.write.domain;

import com.github.maximtereshchenko.clerk.write.api.port.Files;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

final class FilesInMemory implements Files {

    private final Map<UUID, FileDetails> fileDetails = new HashMap<>();

    @Override
    public synchronized void setTimeToLive(UUID id, UUID userId, Instant timeToLive) {
        fileDetails.put(id, fileDetails.get(id).withTimeToLive(timeToLive));
    }

    @Override
    public synchronized InputStream inputStream(UUID id, UUID userId) {
        return new ByteArrayInputStream(fileDetails.get(id).content());
    }

    @Override
    public synchronized void persist(UUID id, UUID userId, Instant timeToLive, byte[] bytes) {
        fileDetails.put(id, new FileDetails(bytes, timeToLive));
    }

    private static final class FileDetails {

        private final byte[] content;
        private final Instant timeToLive;

        FileDetails(byte[] content, Instant timeToLive) {
            this.content = content;
            this.timeToLive = timeToLive;
        }

        FileDetails withTimeToLive(Instant timeToLive) {
            return new FileDetails(content, timeToLive);
        }

        byte[] content() {
            return content;
        }

        Instant timeToLive() {
            return timeToLive;
        }
    }
}
