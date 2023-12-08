package com.github.maximtereshchenko.gateway;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
class ClerkReadController extends UserIdAware implements ClerkReadGatewayApi {

    private final ClerkReadClient clerkReadClient;
    private final Translator translator;

    ClerkReadController(ClerkReadClient clerkReadClient, Translator translator) {
        this.clerkReadClient = clerkReadClient;
        this.translator = translator;
    }

    @Override
    public ResponseEntity<List<ClerkReadGatewayDocument>> documents() {
        return ResponseEntity.ok(
                translator.mapDocuments(
                        clerkReadClient.documents(userId())
                                .getBody()
                )
        );
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
                translator.mapTemplates(
                        clerkReadClient.templates(userId())
                                .getBody()
                )
        );
    }
}
