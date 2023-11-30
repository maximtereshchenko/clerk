package com.github.maximtereshchenko.clerk.write.domain;

import com.github.maximtereshchenko.clerk.write.api.port.Files;
import com.github.maximtereshchenko.clerk.write.api.port.exception.FileBelongsToAnotherUser;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

final class FilesInMemory implements Files {

    private final Map<UUID, FileDetails> fileDetails = new HashMap<>();

    @Override
    public synchronized void setTimeToLive(UUID id, UUID userId, Instant timeToLive) throws FileBelongsToAnotherUser {
        var details = fileDetails.get(id);
        if (!details.belongsTo(userId)) {
            throw new FileBelongsToAnotherUser(id, details.ownerId());
        }
        fileDetails.put(id, details.withTimeToLive(timeToLive));
    }

    @Override
    public synchronized InputStream inputStream(UUID id, UUID userId) {
        return new ByteArrayInputStream(fileDetails.get(id).content());
    }

    @Override
    public synchronized void persist(UUID id, UUID userId, Instant timeToLive, byte[] bytes) {
        fileDetails.put(id, new FileDetails(userId, bytes, timeToLive));
    }

    private static final class FileDetails {

        private final UUID ownerId;
        private final byte[] content;
        private final Instant timeToLive;

        FileDetails(UUID ownerId, byte[] content, Instant timeToLive) {
            this.ownerId = ownerId;
            this.content = content;
            this.timeToLive = timeToLive;
        }

        FileDetails withTimeToLive(Instant timeToLive) {
            return new FileDetails(ownerId, content, timeToLive);
        }

        byte[] content() {
            return content;
        }

        Instant timeToLive() {
            return timeToLive;
        }

        boolean belongsTo(UUID userId) {
            return ownerId.equals(userId);
        }

        UUID ownerId() {
            return ownerId;
        }
    }
}
