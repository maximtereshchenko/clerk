package com.github.maximtereshchenko.clerk.write.api.exception;

import java.util.UUID;

public final class CouldNotFindPlaceholders extends TemplateException {

    public CouldNotFindPlaceholders(UUID templateId, UUID fileId, Throwable cause) {
        super(templateId, fileId, cause);
    }
}
