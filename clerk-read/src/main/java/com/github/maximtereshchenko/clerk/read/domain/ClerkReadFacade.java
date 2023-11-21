package com.github.maximtereshchenko.clerk.read.domain;

import com.github.maximtereshchenko.clerk.event.TemplateCreated;
import com.github.maximtereshchenko.clerk.read.api.ClerkReadModule;
import com.github.maximtereshchenko.clerk.read.api.PlaceholdersPresentation;
import com.github.maximtereshchenko.clerk.read.api.TemplatePresentation;
import com.github.maximtereshchenko.clerk.read.api.port.Placeholders;
import com.github.maximtereshchenko.clerk.read.api.port.Templates;

import java.util.Collection;
import java.util.UUID;

public final class ClerkReadFacade implements ClerkReadModule {

    private final Templates templates;
    private final Placeholders placeholders;

    public ClerkReadFacade(Templates templates, Placeholders placeholders) {
        this.templates = templates;
        this.placeholders = placeholders;
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
                new PlaceholdersPresentation(templateCreated.id(), templateCreated.placeholders(), templateCreated.timestamp())
        );
    }

    @Override
    public Collection<TemplatePresentation> templates() {
        return templates.findAll();
    }

    @Override
    public PlaceholdersPresentation placeholders(UUID id) {
        return placeholders.findById(id).orElseThrow();
    }

    private boolean isOld(TemplateCreated templateCreated) {
        return templates.findById(templateCreated.id())
                .map(templatePresentation -> !templateCreated.timestamp().isAfter(templatePresentation.timestamp()))
                .orElse(Boolean.FALSE);
    }
}
