package com.github.maximtereshchenko.clerk.write.api.exception;

import java.util.UUID;

public final class TemplateIsEmpty extends Exception {

    public TemplateIsEmpty(UUID templateId, UUID fileId) {
        super("Template ID %s, file ID %s".formatted(templateId, fileId));
    }
}
