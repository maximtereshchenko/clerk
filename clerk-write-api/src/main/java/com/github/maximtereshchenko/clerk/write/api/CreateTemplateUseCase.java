package com.github.maximtereshchenko.clerk.write.api;

import com.github.maximtereshchenko.clerk.write.api.exception.IdIsTaken;
import com.github.maximtereshchenko.clerk.write.api.exception.NameIsRequired;
import com.github.maximtereshchenko.clerk.write.api.exception.TemplateIsEmpty;
import com.github.maximtereshchenko.clerk.write.api.port.exception.CouldNotFindFile;
import com.github.maximtereshchenko.clerk.write.api.port.exception.CouldNotProcessFile;
import com.github.maximtereshchenko.clerk.write.api.port.exception.FileIsExpired;

import java.io.IOException;
import java.util.UUID;

public interface CreateTemplateUseCase {

    void createTemplate(UUID id, UUID fileId, String name) throws IOException, NameIsRequired, IdIsTaken, FileIsExpired, CouldNotFindFile, TemplateIsEmpty, CouldNotProcessFile;
}
