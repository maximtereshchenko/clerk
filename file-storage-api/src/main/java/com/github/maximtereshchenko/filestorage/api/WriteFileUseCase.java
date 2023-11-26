package com.github.maximtereshchenko.filestorage.api;

import com.github.maximtereshchenko.filestorage.api.exception.CouldNotFindFile;

import java.io.OutputStream;
import java.util.UUID;

public interface WriteFileUseCase {

    void writeFile(UUID id, OutputStream outputStream) throws CouldNotFindFile;
}
