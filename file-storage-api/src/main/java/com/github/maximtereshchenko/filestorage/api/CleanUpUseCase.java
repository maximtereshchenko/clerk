package com.github.maximtereshchenko.filestorage.api;

import java.io.IOException;

public interface CleanUpUseCase {

    void cleanUp() throws IOException;
}
