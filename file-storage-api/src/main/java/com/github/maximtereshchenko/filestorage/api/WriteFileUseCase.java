package com.github.maximtereshchenko.filestorage.api;

import java.io.OutputStream;
import java.util.UUID;

public interface WriteFileUseCase {

    void writeFile(UUID id, OutputStream outputStream);
}
