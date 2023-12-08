package com.github.maximtereshchenko.gateway;

import com.github.maximtereshchenko.clerk.write.CreateTemplateCommand;
import com.github.maximtereshchenko.outbox.Outbox;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
class ClerkWriteController extends UserIdAware implements ClerkWriteGatewayApi {

    private final Outbox outbox;
    private final String topic;

    ClerkWriteController(Outbox outbox, String topic) {
        this.outbox = outbox;
        this.topic = topic;
    }

    @Override
    public ResponseEntity<Void> createTemplate(ClerkWriteGatewayCreateTemplateRequest request) {
        outbox.put(
                request.getId().toString(),
                new CreateTemplateCommand(
                        request.getId().toString(),
                        userId().toString(),
                        request.getFileId().toString(),
                        request.getName()
                ),
                topic
        );
        return ResponseEntity.accepted().build();
    }
}
