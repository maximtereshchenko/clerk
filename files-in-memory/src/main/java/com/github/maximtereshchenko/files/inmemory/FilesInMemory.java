package com.github.maximtereshchenko.files.inmemory;

import com.github.maximtereshchenko.clerk.write.api.port.CouldNotFindFile;
import com.github.maximtereshchenko.clerk.write.api.port.Files;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.file.Path;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class FilesInMemory implements Files {

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
        try {
            return java.nio.file.Files.newInputStream(fileDetails.get(id).path());
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public synchronized void save(UUID id, Path path, Instant timeToLive) {
        fileDetails.put(id, new FileDetails(path, timeToLive));
    }

    public synchronized Instant timeToLive(UUID id) {
        return fileDetails.get(id).timeToLive();
    }

    private record FileDetails(Path path, Instant timeToLive) {

        FileDetails withTimeToLive(Instant timeToLive) {
            return new FileDetails(path, timeToLive);
        }
    }
}
