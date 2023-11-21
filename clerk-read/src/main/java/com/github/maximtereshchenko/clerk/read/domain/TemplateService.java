package com.github.maximtereshchenko.clerk.read.domain;

import com.github.maximtereshchenko.clerk.event.TemplateCreated;
import com.github.maximtereshchenko.clerk.read.api.TemplatePresentation;
import com.github.maximtereshchenko.clerk.read.api.UpdateTemplatesUseCase;
import com.github.maximtereshchenko.clerk.read.api.ViewTemplatesUseCase;
import com.github.maximtereshchenko.clerk.read.api.port.Templates;

import java.util.Collection;

final class TemplateService implements UpdateTemplatesUseCase, ViewTemplatesUseCase {

    private final Templates templates;

    TemplateService(Templates templates) {
        this.templates = templates;
    }

    @Override
    public void onTemplateCreated(TemplateCreated templateCreated) {
        templates.persist(new TemplatePresentation(templateCreated.id(), templateCreated.name()));
    }

    @Override
    public Collection<TemplatePresentation> templates() {
        return templates.findAll();
    }
}
