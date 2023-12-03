package com.github.maximtereshchenko.gateway;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
class ClerkReadController implements ClerkReadGatewayApi {

    private final ClerkReadClient clerkReadClient;
    private final Translator translator;

    ClerkReadController(ClerkReadClient clerkReadClient, Translator translator) {
        this.clerkReadClient = clerkReadClient;
        this.translator = translator;
    }

    @Override
    public ResponseEntity<ClerkReadGatewayPlaceholders> placeholders(UUID templateId) {
        return ResponseEntity.ok(
                translator.map(
                        clerkReadClient.placeholders(userId(), templateId)
                                .getBody()
                )
        );
    }

    @Override
    public ResponseEntity<List<ClerkReadGatewayTemplate>> templates() {
        return ResponseEntity.ok(
                translator.map(
                        clerkReadClient.templates(userId())
                                .getBody()
                )
        );
    }

    private UUID userId() {
        return UUID.fromString(principal().getSubject());
    }

    private Jwt principal() {
        return (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
