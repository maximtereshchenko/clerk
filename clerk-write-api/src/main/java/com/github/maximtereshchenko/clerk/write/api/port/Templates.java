package com.github.maximtereshchenko.clerk.write.api.port;

import java.util.Optional;
import java.util.UUID;

public interface Templates {

    void persist(PersistentTemplate persistentTemplate);

    Optional<PersistentTemplate> findById(UUID id);

    boolean exists(UUID id);
}
