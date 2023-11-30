package com.github.maximtereshchenko.clerk.read.api;

import java.util.Collection;
import java.util.UUID;

public interface ViewTemplatesUseCase {

    Collection<TemplatePresentation> templates(UUID userId);
}
