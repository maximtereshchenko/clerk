package com.github.maximtereshchenko.clerk.read.domain;

import com.github.maximtereshchenko.clerk.event.TemplateCreated;
import com.github.maximtereshchenko.clerk.read.api.ClerkReadModule;
import com.github.maximtereshchenko.clerk.read.api.TemplatePresentation;
import com.github.maximtereshchenko.clerk.read.api.port.Templates;

import java.util.Collection;

public final class ClerkReadFacade implements ClerkReadModule {

    private final TemplateService templateService;

    public ClerkReadFacade(Templates templates) {
        templateService = new TemplateService(templates);
    }

    @Override
    public void onTemplateCreated(TemplateCreated templateCreated) {
        templateService.onTemplateCreated(templateCreated);
    }

    @Override
    public Collection<TemplatePresentation> templates() {
        return templateService.templates();
    }
}
