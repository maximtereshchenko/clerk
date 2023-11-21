package com.github.maximtereshchenko.clerk.write.api.exception;

import java.util.UUID;

public final class NameIsRequired extends Exception {

    public NameIsRequired(UUID templateId) {
        super(templateId.toString());
    }
}
