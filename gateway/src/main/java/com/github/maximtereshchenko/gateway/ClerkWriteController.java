package com.github.maximtereshchenko.gateway;

import com.github.maximtereshchenko.clerk.write.CreateDocumentCommand;
import com.github.maximtereshchenko.clerk.write.CreateTemplateCommand;
import com.github.maximtereshchenko.outbox.Outbox;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.Collectors;

@RestController
class ClerkWriteController extends UserIdAware implements ClerkWriteGatewayApi {

    private final Outbox outbox;
    private final String createTemplateCommandTopic;
    private final String createDocumentCommandTopic;

    ClerkWriteController(
            Outbox outbox,
            String createTemplateCommandTopic,
            String createDocumentCommandTopic
    ) {
        this.outbox = outbox;
        this.createTemplateCommandTopic = createTemplateCommandTopic;
        this.createDocumentCommandTopic = createDocumentCommandTopic;
    }

    @Override
    public ResponseEntity<Void> createDocument(ClerkWriteGatewayCreateDocumentRequest request) {
        outbox.put(
                request.getId().toString(),
                new CreateDocumentCommand(
                        request.getId().toString(),
                        userId().toString(),
                        request.getTemplateId().toString(),
                        request.getValues()
                                .stream()
                                .collect(
                                        Collectors.toMap(
                                                ClerkWriteGatewayCreateDocumentRequestValuesInner::getKey,
                                                ClerkWriteGatewayCreateDocumentRequestValuesInner::getValue
                                        )
                                )
                ),
                createDocumentCommandTopic
        );
        return ResponseEntity.accepted().build();
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
                createTemplateCommandTopic
        );
        return ResponseEntity.accepted().build();
    }
}
