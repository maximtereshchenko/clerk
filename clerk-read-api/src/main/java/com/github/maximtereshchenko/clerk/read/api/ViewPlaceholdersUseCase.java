package com.github.maximtereshchenko.clerk.read.api;

import com.github.maximtereshchenko.clerk.read.api.exception.CouldNotFindTemplate;
import com.github.maximtereshchenko.clerk.read.api.exception.TemplateBelongsToAnotherUser;

import java.util.UUID;

public interface ViewPlaceholdersUseCase {

    PlaceholdersPresentation placeholders(UUID id, UUID userId)
            throws TemplateBelongsToAnotherUser, CouldNotFindTemplate;
}
