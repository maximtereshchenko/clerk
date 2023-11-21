package com.github.xini1.clerk.write.api;

import com.github.xini1.clerk.write.api.exception.CouldNotExtendTimeToLive;
import java.util.UUID;

public interface CreateTemplateUseCase {

    void createTemplate(UUID id, UUID fileId, String name) throws CouldNotExtendTimeToLive;
}
