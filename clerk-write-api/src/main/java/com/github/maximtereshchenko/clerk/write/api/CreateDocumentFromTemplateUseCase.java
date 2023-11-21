package com.github.maximtereshchenko.clerk.write.api;

import com.github.maximtereshchenko.clerk.write.api.exception.CouldNotFindTemplate;
import com.github.maximtereshchenko.clerk.write.api.exception.ValuesAreRequired;

import java.util.Map;
import java.util.UUID;

public interface CreateDocumentFromTemplateUseCase {

    void createDocument(UUID id, UUID templateId, Map<String, String> values)
            throws CouldNotFindTemplate, ValuesAreRequired;
}
