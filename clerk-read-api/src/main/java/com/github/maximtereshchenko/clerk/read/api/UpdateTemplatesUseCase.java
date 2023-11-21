package com.github.maximtereshchenko.clerk.read.api;

import com.github.maximtereshchenko.clerk.event.TemplateCreated;

public interface UpdateTemplatesUseCase {

    void onTemplateCreated(TemplateCreated templateCreated);
}
