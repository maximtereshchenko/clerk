package com.github.maximtereshchenko.filestorage.api.port;

import java.util.UUID;

public interface FileLabels {

    void persist(FileLabel fileLabel);

    boolean exists(UUID id);
}
