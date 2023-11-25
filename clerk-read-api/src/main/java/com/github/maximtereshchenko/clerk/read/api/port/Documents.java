package com.github.maximtereshchenko.clerk.read.api.port;

import com.github.maximtereshchenko.clerk.read.api.DocumentPresentation;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

public interface Documents {

    void persist(DocumentPresentation documentPresentation);

    Collection<DocumentPresentation> findAll();

    Optional<DocumentPresentation> findByFileId(UUID fileId);
}
