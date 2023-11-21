package com.github.maximtereshchenko.clerk.write.files.inmemory;

import java.time.Instant;

final class FileDetails {

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
