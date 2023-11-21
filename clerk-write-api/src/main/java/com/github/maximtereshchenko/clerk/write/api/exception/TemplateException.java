package com.github.maximtereshchenko.clerk.write.api.exception;

import java.util.UUID;

abstract class TemplateException extends Exception {

    TemplateException(UUID templateId, UUID fileId, Throwable cause) {
        super("Template ID %s, file ID %s".formatted(templateId, fileId), cause);
    }
}
