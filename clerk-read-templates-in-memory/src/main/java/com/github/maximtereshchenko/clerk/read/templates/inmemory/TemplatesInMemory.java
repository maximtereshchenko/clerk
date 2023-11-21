package com.github.maximtereshchenko.clerk.read.templates.inmemory;

import com.github.maximtereshchenko.clerk.read.api.TemplatePresentation;
import com.github.maximtereshchenko.clerk.read.api.port.Templates;

import java.util.*;

public final class TemplatesInMemory implements Templates {

    private final Map<UUID, TemplatePresentation> map = new HashMap<>();

    @Override
    public synchronized void persist(TemplatePresentation templatePresentation) {
        map.put(templatePresentation.id(), templatePresentation);
    }

    @Override
    public synchronized Collection<TemplatePresentation> findAll() {
        return List.copyOf(map.values());
    }

    @Override
    public synchronized Optional<TemplatePresentation> findById(UUID id) {
        return Optional.ofNullable(map.get(id));
    }
}
