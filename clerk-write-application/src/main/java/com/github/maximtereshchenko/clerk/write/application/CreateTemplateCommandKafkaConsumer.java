package com.github.maximtereshchenko.clerk.write.application;

import com.github.maximtereshchenko.clerk.write.CreateTemplateCommand;
import com.github.maximtereshchenko.clerk.write.CreateTemplateResult;
import com.github.maximtereshchenko.clerk.write.Result;
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
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;

import java.io.IOException;
import java.util.UUID;

final class CreateTemplateCommandKafkaConsumer {

    private final CreateTemplateUseCase useCase;
    private final Outbox outbox;
    private final String createTemplateResultTopic;

    CreateTemplateCommandKafkaConsumer(CreateTemplateUseCase useCase, Outbox outbox, String createTemplateResultTopic) {
        this.useCase = useCase;
        this.outbox = outbox;
        this.createTemplateResultTopic = createTemplateResultTopic;
    }

    @KafkaListener(topics = "${clerk.write.create-template-command.topic}")
    void onCreateTemplateCommand(@Header(KafkaHeaders.RECEIVED_KEY) String key, @Payload CreateTemplateCommand command) {
        try {
            useCase.createTemplate(
                    UUID.fromString(command.getId()),
                    UUID.fromString(command.getUserId()),
                    UUID.fromString(command.getFileId()),
                    command.getName()
            );
            outbox.put(new Message(key, new CreateTemplateResult(command, Result.CREATED), createTemplateResultTopic));
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (NameIsRequired e) {
            throw new RuntimeException(e);
        } catch (IdIsTaken e) {
            throw new RuntimeException(e);
        } catch (FileIsExpired e) {
            throw new RuntimeException(e);
        } catch (CouldNotFindFile e) {
            throw new RuntimeException(e);
        } catch (TemplateIsEmpty e) {
            throw new RuntimeException(e);
        } catch (CouldNotProcessFile e) {
            throw new RuntimeException(e);
        } catch (FileBelongsToAnotherUser e) {
            throw new RuntimeException(e);
        }
    }
}
