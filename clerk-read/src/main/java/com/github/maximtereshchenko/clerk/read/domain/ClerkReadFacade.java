package com.github.maximtereshchenko.clerk.read.domain;

import com.github.maximtereshchenko.clerk.event.DocumentCreated;
import com.github.maximtereshchenko.clerk.event.TemplateCreated;
import com.github.maximtereshchenko.clerk.read.api.ClerkReadModule;
import com.github.maximtereshchenko.clerk.read.api.DocumentPresentation;
import com.github.maximtereshchenko.clerk.read.api.PlaceholdersPresentation;
import com.github.maximtereshchenko.clerk.read.api.TemplatePresentation;
import com.github.maximtereshchenko.clerk.read.api.port.Documents;
import com.github.maximtereshchenko.clerk.read.api.port.Placeholders;
import com.github.maximtereshchenko.clerk.read.api.port.Templates;

import java.util.Collection;
import java.util.UUID;

public final class ClerkReadFacade implements ClerkReadModule {

    private final Templates templates;
    private final Placeholders placeholders;
    private final Documents documents;

    public ClerkReadFacade(Templates templates, Placeholders placeholders, Documents documents) {
        this.templates = templates;
        this.placeholders = placeholders;
        this.documents = documents;
    }

    @Override
    public void onTemplateCreated(TemplateCreated templateCreated) {
        if (isOld(templateCreated)) {
            return;
        }
        templates.persist(
                new TemplatePresentation(templateCreated.id(), templateCreated.name(), templateCreated.timestamp())
        );
        placeholders.persist(
                new PlaceholdersPresentation(
                        templateCreated.id(),
                        templateCreated.placeholders(),
                        templateCreated.timestamp()
                )
        );
    }

    @Override
    public Collection<TemplatePresentation> templates(UUID userId) {
        return templates.findAll();
    }

    @Override
    public PlaceholdersPresentation placeholders(UUID id, UUID userId) {
        return placeholders.findById(id).orElseThrow();
    }

    @Override
    public void onDocumentCreated(DocumentCreated documentCreated) {
        if (isOld(documentCreated)) {
            return;
        }
        documents.persist(
                new DocumentPresentation(
                        documentCreated.fileId(),
                        documentCreated.timeToLive(),
                        documentCreated.timestamp()
                )
        );
    }

    @Override
    public Collection<DocumentPresentation> documents(UUID userId) {
        return documents.findAll();
    }

    private boolean isOld(DocumentCreated documentCreated) {
        return documents.findByFileId(documentCreated.fileId())
                .map(templatePresentation -> !documentCreated.timestamp().isAfter(templatePresentation.timestamp()))
                .orElse(Boolean.FALSE);
    }

    private boolean isOld(TemplateCreated templateCreated) {
        return templates.findById(templateCreated.id())
                .map(templatePresentation -> !templateCreated.timestamp().isAfter(templatePresentation.timestamp()))
                .orElse(Boolean.FALSE);
    }
}
