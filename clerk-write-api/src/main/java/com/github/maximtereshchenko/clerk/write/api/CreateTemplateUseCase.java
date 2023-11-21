package com.github.maximtereshchenko.clerk.write.api;

import com.github.maximtereshchenko.clerk.write.api.exception.*;

import java.util.UUID;

public interface CreateTemplateUseCase {

    void createTemplate(UUID id, UUID fileId, String name)
            throws CouldNotExtendTimeToLive,
            CouldNotFindPlaceholders,
            TemplateIsEmpty,
            NameIsRequired,
            TemplateWithIdExists;
}
