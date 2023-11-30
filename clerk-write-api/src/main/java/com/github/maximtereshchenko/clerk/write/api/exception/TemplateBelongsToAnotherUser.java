package com.github.maximtereshchenko.clerk.write.api.exception;

import java.util.UUID;

public final class TemplateBelongsToAnotherUser extends Exception {

    public TemplateBelongsToAnotherUser(UUID id, UUID ownerId) {
        super("Template ID: %s, owner ID: %s".formatted(id, ownerId));
    }
}
