package com.github.maximtereshchenko.clerk.write.api.exception;

import java.util.UUID;

public final class TemplateWithIdExists extends Exception {

    public TemplateWithIdExists(UUID templateId) {
        super(templateId.toString());
    }
}
