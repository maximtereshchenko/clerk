package com.github.maximtereshchenko.clerk.read.api.port;

import com.github.maximtereshchenko.clerk.read.api.TemplatePresentation;

import java.util.Collection;

public interface Templates {

    void persist(TemplatePresentation templatePresentation);

    Collection<TemplatePresentation> findAll();
}
