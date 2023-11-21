package com.github.maximtereshchenko.eventstore.inmemory;

import com.github.maximtereshchenko.clerk.write.api.port.PersistentTemplate;
import com.github.maximtereshchenko.clerk.write.api.port.Templates;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public final class TemplatesInMemory implements Templates {

    private final Map<UUID, PersistentTemplate> map = new HashMap<>();

    @Override
    public synchronized void persist(PersistentTemplate persistentTemplate) {
        map.put(persistentTemplate.id(), persistentTemplate);
    }

    @Override
    public synchronized Optional<PersistentTemplate> findById(UUID id) {
        return Optional.ofNullable(map.get(id));
    }
}
