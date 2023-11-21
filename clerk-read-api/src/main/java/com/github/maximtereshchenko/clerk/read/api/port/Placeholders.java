package com.github.maximtereshchenko.clerk.read.api.port;

import com.github.maximtereshchenko.clerk.read.api.PlaceholdersPresentation;

import java.util.Optional;
import java.util.UUID;

public interface Placeholders {

    void persist(PlaceholdersPresentation placeholders);

    Optional<PlaceholdersPresentation> findById(UUID id);
}
