package com.github.maximtereshchenko.clerk.write.domain;

import com.github.maximtereshchenko.clerk.write.api.port.Files;
import com.github.maximtereshchenko.clerk.write.api.port.exception.CouldNotFindFile;

import java.io.*;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

final class FilesInMemory implements Files {

    private final Map<UUID, FileDetails> fileDetails = new HashMap<>();

    @Override
    public synchronized void setTimeToLive(UUID id, Instant timeToLive) throws CouldNotFindFile {
        if (!fileDetails.containsKey(id)) {
            throw new CouldNotFindFile(id);
        }
        fileDetails.put(id, fileDetails.get(id).withTimeToLive(timeToLive));
    }

    @Override
    public synchronized InputStream inputStream(UUID id) {
        return new ByteArrayInputStream(fileDetails.get(id).content());
    }

    @Override
    public synchronized void persist(UUID id, Instant timeToLive, OutputStreamConsumer consumer) {

        fileDetails.put(id, new FileDetails(bytes(consumer), timeToLive));
    }

    public synchronized Instant timeToLive(UUID id) {
        return fileDetails.get(id).timeToLive();
    }

    private byte[] bytes(OutputStreamConsumer consumer) {
        try {
            var outputStream = new ByteArrayOutputStream();
            consumer.accept(outputStream);
            return outputStream.toByteArray();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
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
