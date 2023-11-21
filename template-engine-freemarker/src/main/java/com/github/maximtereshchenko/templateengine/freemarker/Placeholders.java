package com.github.maximtereshchenko.templateengine.freemarker;

import freemarker.template.TemplateHashModel;
import freemarker.template.TemplateModel;

import java.util.HashSet;
import java.util.Set;

final class Placeholders implements TemplateHashModel {

    private final Set<String> collected = new HashSet<>();

    @Override
    public TemplateModel get(String key) {
        collected.add(key);
        return TemplateModel.NOTHING;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    Set<String> collected() {
        return Set.copyOf(collected);
    }
}
