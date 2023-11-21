package com.github.maximtereshchenko.clerk.write.api.exception;

import java.util.UUID;

public final class TemplateIsEmpty extends TemplateException {

    public TemplateIsEmpty(UUID templateId, UUID fileId) {
        super(templateId, fileId, null);
    }
}
