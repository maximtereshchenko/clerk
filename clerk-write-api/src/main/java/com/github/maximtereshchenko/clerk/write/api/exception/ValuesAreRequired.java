package com.github.maximtereshchenko.clerk.write.api.exception;

import java.util.UUID;

public final class ValuesAreRequired extends Exception {

    public ValuesAreRequired(UUID documentId) {
        super(documentId.toString());
    }
}
