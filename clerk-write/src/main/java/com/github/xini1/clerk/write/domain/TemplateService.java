package com.github.xini1.clerk.write.domain;

import com.github.xini1.clerk.write.api.CreateTemplateUseCase;
import com.github.xini1.clerk.write.api.port.Files;
import java.time.Instant;
import java.util.UUID;

final class TemplateService implements CreateTemplateUseCase {

    private final Files files;

    TemplateService(Files files) {
        this.files = files;
    }

    @Override
    public void createTemplate(UUID id, UUID fileId, String name) {
        files.setTimeToLive(fileId, Instant.MAX);
    }
}
