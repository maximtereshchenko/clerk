package com.github.maximtereshchenko.clerk.write.api.exception;

import java.util.UUID;

public final class CouldNotFindTemplate extends Exception {

    public CouldNotFindTemplate(UUID documentId, UUID templateId) {
        super("Document ID: %s, template ID: %s".formatted(documentId, templateId));
    }
}
