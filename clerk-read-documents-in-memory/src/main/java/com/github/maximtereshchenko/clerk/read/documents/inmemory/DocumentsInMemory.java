package com.github.maximtereshchenko.clerk.read.documents.inmemory;

import com.github.maximtereshchenko.clerk.read.api.DocumentPresentation;
import com.github.maximtereshchenko.clerk.read.api.port.Documents;

import java.util.*;

public final class DocumentsInMemory implements Documents {

    private final Map<UUID, DocumentPresentation> map = new HashMap<>();

    @Override
    public synchronized void persist(DocumentPresentation documentPresentation) {
        map.put(documentPresentation.fileId(), documentPresentation);
    }

    @Override
    public synchronized Collection<DocumentPresentation> findAll() {
        return List.copyOf(map.values());
    }

    @Override
    public synchronized Optional<DocumentPresentation> findByFileId(UUID fileId) {
        return Optional.ofNullable(map.get(fileId));
    }
}
