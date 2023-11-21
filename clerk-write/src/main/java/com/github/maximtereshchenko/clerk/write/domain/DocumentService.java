package com.github.maximtereshchenko.clerk.write.domain;

import com.github.maximtereshchenko.clerk.write.api.CreateDocumentFromTemplateUseCase;
import com.github.maximtereshchenko.clerk.write.api.exception.CouldNotFindTemplate;
import com.github.maximtereshchenko.clerk.write.api.exception.ValuesAreRequired;
import com.github.maximtereshchenko.clerk.write.api.port.Files;
import com.github.maximtereshchenko.clerk.write.api.port.Templates;

import java.util.Map;
import java.util.UUID;

final class DocumentService implements CreateDocumentFromTemplateUseCase {

    private final Templates templates;
    private final Files files;

    DocumentService(Templates templates, Files files) {
        this.templates = templates;
        this.files = files;
    }

    @Override
    public void createDocument(UUID id, UUID templateId, Map<String, String> values)
            throws CouldNotFindTemplate, ValuesAreRequired {
        if (values.isEmpty()) {
            throw new ValuesAreRequired(id);
        }
        templates.findById(templateId).orElseThrow(() -> new CouldNotFindTemplate(id, templateId));
    }
}
