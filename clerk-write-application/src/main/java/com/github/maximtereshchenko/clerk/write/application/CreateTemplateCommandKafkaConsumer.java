package com.github.maximtereshchenko.clerk.write.application;

import com.github.maximtereshchenko.clerk.write.CreateTemplateCommand;
import com.github.maximtereshchenko.clerk.write.CreateTemplateResult;
import com.github.maximtereshchenko.clerk.write.CreateTemplateResultResponse;
import com.github.maximtereshchenko.clerk.write.api.CreateTemplateUseCase;
import com.github.maximtereshchenko.clerk.write.api.exception.IdIsTaken;
import com.github.maximtereshchenko.clerk.write.api.exception.NameIsRequired;
import com.github.maximtereshchenko.clerk.write.api.exception.TemplateIsEmpty;
import com.github.maximtereshchenko.clerk.write.api.port.exception.CouldNotFindFile;
import com.github.maximtereshchenko.clerk.write.api.port.exception.CouldNotProcessFile;
import com.github.maximtereshchenko.clerk.write.api.port.exception.FileBelongsToAnotherUser;
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

final class CreateTemplateCommandKafkaConsumer {

    private static final Logger LOG = LoggerFactory.getLogger(CreateTemplateCommandKafkaConsumer.class);

    private final CreateTemplateUseCase useCase;
    private final Outbox outbox;
    private final String responseTopic;

    CreateTemplateCommandKafkaConsumer(CreateTemplateUseCase useCase, Outbox outbox, String responseTopic) {
        this.useCase = useCase;
        this.outbox = outbox;
        this.responseTopic = responseTopic;
    }

    @KafkaListener(topics = "${clerk.write.create-template-command.topic}")
    void onCreateTemplateCommand(
            @Header(KafkaHeaders.RECEIVED_KEY) String key,
            @Payload CreateTemplateCommand command
    ) throws IOException {
        outbox.put(
                new Message(
                        key,
                        new CreateTemplateResultResponse(command, createTemplate(command)),
                        responseTopic
                )
        );
    }

    private CreateTemplateResult createTemplate(CreateTemplateCommand command) throws IOException {
        try {
            useCase.createTemplate(
                    UUID.fromString(command.getId()),
                    UUID.fromString(command.getUserId()),
                    UUID.fromString(command.getFileId()),
                    command.getName()
            );
            return CreateTemplateResult.CREATED;
        } catch (FileIsExpired e) {
            log(e);
            return CreateTemplateResult.FILE_IS_EXPIRED;
        } catch (TemplateIsEmpty e) {
            log(e);
            return CreateTemplateResult.TEMPLATE_IS_EMPTY;
        } catch (NameIsRequired e) {
            log(e);
            return CreateTemplateResult.NAME_IS_REQUIRED;
        } catch (FileBelongsToAnotherUser e) {
            log(e);
            return CreateTemplateResult.FILE_BELONGS_TO_ANOTHER_USER;
        } catch (IdIsTaken e) {
            log(e);
            return CreateTemplateResult.ID_IS_TAKEN;
        } catch (CouldNotProcessFile e) {
            log(e);
            return CreateTemplateResult.COULD_NOT_PROCESS_FILE;
        } catch (CouldNotFindFile e) {
            log(e);
            return CreateTemplateResult.COULD_NOT_FIND_FILE;
        }
    }

    private void log(Exception e) {
        LOG.warn("Could not create template", e);
    }
}
