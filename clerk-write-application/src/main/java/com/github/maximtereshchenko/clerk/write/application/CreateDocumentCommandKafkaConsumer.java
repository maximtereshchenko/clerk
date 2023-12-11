package com.github.maximtereshchenko.clerk.write.application;

import com.github.maximtereshchenko.clerk.write.CreateDocumentCommand;
import com.github.maximtereshchenko.clerk.write.CreateDocumentResult;
import com.github.maximtereshchenko.clerk.write.CreateDocumentResultResponse;
import com.github.maximtereshchenko.clerk.write.api.CreateDocumentFromTemplateUseCase;
import com.github.maximtereshchenko.clerk.write.api.exception.CouldNotFindTemplate;
import com.github.maximtereshchenko.clerk.write.api.exception.TemplateBelongsToAnotherUser;
import com.github.maximtereshchenko.clerk.write.api.exception.ValuesAreRequired;
import com.github.maximtereshchenko.clerk.write.api.port.exception.CouldNotFindFile;
import com.github.maximtereshchenko.clerk.write.api.port.exception.CouldNotProcessFile;
import com.github.maximtereshchenko.clerk.write.api.port.exception.FileIdIsTaken;
import com.github.maximtereshchenko.clerk.write.api.port.exception.FileIsExpired;
import com.github.maximtereshchenko.outbox.Message;
import com.github.maximtereshchenko.outbox.Outbox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;

import java.io.IOException;
import java.util.UUID;

final class CreateDocumentCommandKafkaConsumer {

    private static final Logger LOG = LoggerFactory.getLogger(CreateDocumentCommandKafkaConsumer.class);

    private final CreateDocumentFromTemplateUseCase useCase;
    private final Outbox outbox;
    private final String responseTopic;

    CreateDocumentCommandKafkaConsumer(CreateDocumentFromTemplateUseCase useCase, Outbox outbox, String responseTopic) {
        this.useCase = useCase;
        this.outbox = outbox;
        this.responseTopic = responseTopic;
    }

    @KafkaListener(topics = "${clerk.write.create-document-command.topic}")
    void onCreateDocumentCommand(
            @Header(KafkaHeaders.RECEIVED_KEY) String key,
            @Payload CreateDocumentCommand command
    ) throws IOException {
        outbox.put(
                new Message(
                        key,
                        new CreateDocumentResultResponse(command, createTemplate(command)),
                        responseTopic
                )
        );
    }

    private CreateDocumentResult createTemplate(CreateDocumentCommand command) throws IOException {
        try {
            useCase.createDocument(
                    UUID.fromString(command.getId()),
                    UUID.fromString(command.getUserId()),
                    UUID.fromString(command.getTemplateId()),
                    command.getValues()
            );
        } catch (ValuesAreRequired e) {
            log(e);
        } catch (FileIdIsTaken e) {
            log(e);
        } catch (CouldNotFindTemplate e) {
            log(e);
        } catch (FileIsExpired e) {
            log(e);
        } catch (CouldNotFindFile e) {
            log(e);
        } catch (CouldNotProcessFile e) {
            log(e);
        } catch (TemplateBelongsToAnotherUser e) {
            log(e);
        }
        return CreateDocumentResult.CREATED;
    }

    private void log(Exception e) {
        LOG.warn("Could not create document", e);
    }
}
