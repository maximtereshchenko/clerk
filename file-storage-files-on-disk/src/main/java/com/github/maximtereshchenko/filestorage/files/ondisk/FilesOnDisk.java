package com.github.maximtereshchenko.filestorage.files.ondisk;

import com.github.maximtereshchenko.filestorage.api.port.Files;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UncheckedIOException;
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
    public Set<UUID> findAll() {
        try (var stream = java.nio.file.Files.walk(directory, 0)) {
            return stream.filter(java.nio.file.Files::isRegularFile)
                    .map(Path::getFileName)
                    .map(Path::toString)
                    .map(UUID::fromString)
                    .collect(Collectors.toSet());
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Override
    public void remove(UUID id) {

    }
}
