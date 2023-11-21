package com.github.maximtereshchenko.clerk.write.api.exception;

import java.util.UUID;

public final class CouldNotFindPlaceholders extends Exception {

    public CouldNotFindPlaceholders(UUID templateId, UUID fileId, Throwable cause) {
        super("Template ID %s, file ID %s".formatted(templateId, fileId), cause);
    }
}
