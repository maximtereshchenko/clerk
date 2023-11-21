package com.github.maximtereshchenko.clerk.write.api;

import com.github.maximtereshchenko.clerk.write.api.exception.CouldNotExtendTimeToLive;
import com.github.maximtereshchenko.clerk.write.api.exception.CouldNotFindPlaceholders;
import com.github.maximtereshchenko.clerk.write.api.exception.NameIsRequired;
import com.github.maximtereshchenko.clerk.write.api.exception.TemplateIsEmpty;

import java.util.UUID;

public interface CreateTemplateUseCase {

    void createTemplate(UUID id, UUID fileId, String name)
            throws CouldNotExtendTimeToLive, CouldNotFindPlaceholders, TemplateIsEmpty, NameIsRequired;
}
