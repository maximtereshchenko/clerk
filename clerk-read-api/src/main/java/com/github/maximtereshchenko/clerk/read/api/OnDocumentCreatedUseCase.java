package com.github.maximtereshchenko.clerk.read.api;

import com.github.maximtereshchenko.clerk.event.DocumentCreated;

public interface OnDocumentCreatedUseCase {

    void onDocumentCreated(DocumentCreated documentCreated);
}
