package com.github.xini1.files.inmemory;

import com.github.xini1.clerk.write.api.port.CouldNotFindFile;
import com.github.xini1.clerk.write.api.port.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public final class FilesInMemory implements Files {

    private final Map<UUID, Instant> timesToLive = new ConcurrentHashMap<>();

    @Override
    public void setTimeToLive(UUID id, Instant timeToLive) throws CouldNotFindFile {
        if (!timesToLive.containsKey(id)) {
            throw new CouldNotFindFile(id);
        }
        timesToLive.put(id, timeToLive);
    }

    public void save(UUID id, Path path, Instant timeToLive) {
        timesToLive.put(id, timeToLive);
    }

    public Instant timeToLive(UUID id) {
        return timesToLive.get(id);
    }
}
