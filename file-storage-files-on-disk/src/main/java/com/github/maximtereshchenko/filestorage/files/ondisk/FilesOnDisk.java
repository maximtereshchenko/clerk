package com.github.maximtereshchenko.filestorage.files.ondisk;

import com.github.maximtereshchenko.filestorage.api.port.Files;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Path;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public final class FilesOnDisk implements Files {

    private final Path directory;

    public FilesOnDisk(Path directory) {
        this.directory = directory;
    }

    @Override
    public synchronized OutputStream outputStream(UUID id) throws IOException {
        return java.nio.file.Files.newOutputStream(directory.resolve(id.toString()));
    }

    @Override
    public InputStream inputStream(UUID id) throws IOException {
        return java.nio.file.Files.newInputStream(directory.resolve(id.toString()));
    }

    @Override
    public Set<UUID> findAll() throws IOException {
        try (var stream = java.nio.file.Files.list(directory)) {
            return stream.map(Path::getFileName)
                    .map(Path::toString)
                    .map(UUID::fromString)
                    .collect(Collectors.toSet());
        }
    }

    @Override
    public void remove(UUID id) throws IOException {
        java.nio.file.Files.deleteIfExists(directory.resolve(id.toString()));
    }
}
