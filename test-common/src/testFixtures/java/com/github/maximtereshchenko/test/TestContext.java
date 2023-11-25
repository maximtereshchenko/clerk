package com.github.maximtereshchenko.test;

import java.util.Map;

public record TestContext(Map<Class<?>, Object> parameters) {

    boolean supports(Class<?> type) {
        return parameters.containsKey(type);
    }

    Object parameter(Class<?> type) {
        return parameters.get(type);
    }
}
