package com.github.maximtereshchenko.clerk.read.api.port;

import com.github.maximtereshchenko.clerk.read.api.TemplatePresentation;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

public interface Templates {

    void persist(TemplatePresentation templatePresentation);

    Collection<TemplatePresentation> findAllByUserId(UUID userId);

    Optional<TemplatePresentation> findById(UUID id);
}
