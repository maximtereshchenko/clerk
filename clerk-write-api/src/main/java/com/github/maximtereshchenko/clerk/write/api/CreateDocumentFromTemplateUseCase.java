package com.github.maximtereshchenko.clerk.write.api;

import com.github.maximtereshchenko.clerk.write.api.exception.CouldNotFindTemplate;
import com.github.maximtereshchenko.clerk.write.api.exception.TemplateBelongsToAnotherUser;
import com.github.maximtereshchenko.clerk.write.api.exception.ValuesAreRequired;
import com.github.maximtereshchenko.clerk.write.api.port.exception.CouldNotFindFile;
import com.github.maximtereshchenko.clerk.write.api.port.exception.CouldNotProcessFile;
import com.github.maximtereshchenko.clerk.write.api.port.exception.FileIdIsTaken;
import com.github.maximtereshchenko.clerk.write.api.port.exception.FileIsExpired;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

public interface CreateDocumentFromTemplateUseCase {

    void createDocument(UUID id, UUID userId, UUID templateId, Map<String, String> values)
            throws IOException,
            ValuesAreRequired,
            FileIdIsTaken,
            CouldNotFindTemplate,
            FileIsExpired,
            CouldNotFindFile,
            CouldNotProcessFile,
            TemplateBelongsToAnotherUser;
}
