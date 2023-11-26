package com.github.maximtereshchenko.filestorage.filelabels.inmemory;

import com.github.maximtereshchenko.filestorage.api.port.FileLabel;
import com.github.maximtereshchenko.filestorage.api.port.FileLabels;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public final class FileLabelsInMemory implements FileLabels {

    private final Map<UUID, FileLabel> map = new HashMap<>();

    @Override
    public synchronized void persist(FileLabel fileLabel) {
        map.put(fileLabel.id(), fileLabel);
    }

    @Override
    public synchronized void update(FileLabel fileLabel) {
        persist(fileLabel);
    }

    @Override
    public synchronized boolean exists(UUID id) {
        return map.containsKey(id);
    }

    @Override
    public synchronized Optional<FileLabel> findById(UUID id) {
        return Optional.ofNullable(map.get(id));
    }
}
