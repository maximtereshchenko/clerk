package com.github.xini1.clerk.write.domain;

import com.github.xini1.clerk.write.api.ClerkWriteModule;
import com.github.xini1.clerk.write.api.exception.CouldNotExtendTimeToLive;
import com.github.xini1.clerk.write.api.port.Files;
import java.util.UUID;

public final class ClerkWriteFacade implements ClerkWriteModule {

    private final TemplateService templateService;

    public ClerkWriteFacade(Files files) {
        templateService = new TemplateService(files);
    }

    @Override
    public void createTemplate(UUID id, UUID fileId, String name) throws CouldNotExtendTimeToLive {
        templateService.createTemplate(id, fileId, name);
    }
}
