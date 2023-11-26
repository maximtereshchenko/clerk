package com.github.maximtereshchenko.filestorage.api.port;

import java.util.Optional;
import java.util.UUID;

public interface FileLabels {

    void persist(FileLabel fileLabel);

    boolean exists(UUID id);

    Optional<FileLabel> findById(UUID id);
}
