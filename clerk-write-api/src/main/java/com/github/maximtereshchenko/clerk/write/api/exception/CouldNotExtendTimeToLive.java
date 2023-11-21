package com.github.maximtereshchenko.clerk.write.api.exception;

import java.util.UUID;

public final class CouldNotExtendTimeToLive extends TemplateException {

    public CouldNotExtendTimeToLive(UUID templateId, UUID fileId, Throwable cause) {
        super(templateId, fileId, cause);
    }
}
