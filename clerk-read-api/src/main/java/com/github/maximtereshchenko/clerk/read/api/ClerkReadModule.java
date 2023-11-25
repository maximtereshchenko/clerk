package com.github.maximtereshchenko.clerk.read.api;

public interface ClerkReadModule
        extends OnTemplateCreatedUseCase,
        ViewTemplatesUseCase,
        ViewPlaceholdersUseCase,
        OnDocumentCreatedUseCase,
        ViewDocumentsUseCase {}
