package com.github.maximtereshchenko.clerk.read.placeholders.inmemory;

import com.github.maximtereshchenko.clerk.read.api.PlaceholdersPresentation;
import com.github.maximtereshchenko.clerk.read.api.port.Placeholders;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public final class PlaceholdersInMemory implements Placeholders {

    private final Map<UUID, PlaceholdersPresentation> map = new HashMap<>();

    @Override
    public synchronized void persist(PlaceholdersPresentation placeholders) {
        map.put(placeholders.id(), placeholders);
    }

    @Override
    public synchronized Optional<PlaceholdersPresentation> findById(UUID id) {
        return Optional.ofNullable(map.get(id));
    }
}
