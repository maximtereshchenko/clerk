package com.github.maximtereshchenko.clerk.read.api;

import java.util.UUID;

public interface ViewPlaceholdersUseCase {

    PlaceholdersPresentation placeholders(UUID id);
}
