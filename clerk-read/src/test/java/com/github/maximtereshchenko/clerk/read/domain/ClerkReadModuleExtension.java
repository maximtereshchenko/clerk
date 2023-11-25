package com.github.maximtereshchenko.clerk.read.domain;

import com.github.maximtereshchenko.clerk.read.api.ClerkReadModule;
import com.github.maximtereshchenko.clerk.read.documents.inmemory.DocumentsInMemory;
import com.github.maximtereshchenko.clerk.read.placeholders.inmemory.PlaceholdersInMemory;
import com.github.maximtereshchenko.clerk.read.templates.inmemory.TemplatesInMemory;
import com.github.maximtereshchenko.test.TestContext;
import com.github.maximtereshchenko.test.TestContextExtension;

import java.util.Map;

final class ClerkReadModuleExtension extends TestContextExtension {

    @Override
    protected TestContext testContext() {
        return new TestContext(
                Map.of(
                        ClerkReadModule.class,
                        new ClerkReadFacade(
                                new TemplatesInMemory(),
                                new PlaceholdersInMemory(),
                                new DocumentsInMemory()
                        )
                )
        );
    }
}
