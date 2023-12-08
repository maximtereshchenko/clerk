package com.github.maximtereshchenko.gateway;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.UUID;

abstract class UserIdAware {

    UUID userId() {
        return UUID.fromString(principal().getSubject());
    }

    private Jwt principal() {
        return (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
