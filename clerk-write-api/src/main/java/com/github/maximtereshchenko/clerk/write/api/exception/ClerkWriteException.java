package com.github.maximtereshchenko.clerk.write.api.exception;

import java.util.UUID;

abstract class ClerkWriteException extends Exception {

    ClerkWriteException(UUID templateId, UUID fileId, Throwable cause) {
        super("Template ID %s, file ID %s".formatted(templateId, fileId), cause);
    }
}
