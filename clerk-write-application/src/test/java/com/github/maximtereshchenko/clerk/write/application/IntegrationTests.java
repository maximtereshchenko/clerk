package com.github.maximtereshchenko.clerk.write.application;

import com.github.maximtereshchenko.clerk.write.*;
import com.github.maximtereshchenko.clerk.write.api.ClerkWriteModule;
import com.github.maximtereshchenko.clerk.write.api.exception.*;
import com.github.maximtereshchenko.clerk.write.api.port.exception.*;
import com.github.maximtereshchenko.outbox.Message;
import com.github.maximtereshchenko.outbox.Outbox;
import com.github.maximtereshchenko.test.ConfluentPlatformExtension;
import com.github.maximtereshchenko.test.PostgreSqlExtension;
import com.github.maximtereshchenko.test.PredictableUUIDExtension;
import org.apache.avro.generic.GenericRecord;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.KafkaTemplate;

import java.io.IOException;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doThrow;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.NONE,
        properties = {
                "clerk.write.create-template-command.topic=create-template-command",
                "clerk.write.create-template-result-response.topic=create-template-result-response",
                "clerk.write.create-document-command.topic=create-document-command",
                "clerk.write.create-document-result-response.topic=create-document-result-response",
                "spring.kafka.producer.key-serializer=org.apache.kafka.common.serialization.StringSerializer",
                "spring.kafka.producer.value-serializer=io.confluent.kafka.serializers.KafkaAvroSerializer"
        }
)
@ExtendWith({PredictableUUIDExtension.class, ConfluentPlatformExtension.class, PostgreSqlExtension.class})
final class IntegrationTests {

    @MockBean
    private ClerkWriteModule module;
    @Autowired
    private KafkaTemplate<String, GenericRecord> kafkaTemplate;
    @Autowired
    private Outbox outbox;
    @Value("${clerk.write.create-template-command.topic}")
    private String createTemplateCommandTopic;
    @Value("${clerk.write.create-template-result-response.topic}")
    private String createTemplateResultResponseTopic;
    @Value("${clerk.write.create-document-command.topic}")
    private String createDocumentCommandTopic;
    @Value("${clerk.write.create-document-result-response.topic}")
    private String createDocumentResultResponseTopic;

    static Stream<Arguments> createTemplateExceptions() {
        return Stream.of(
                arguments(NameIsRequired.class, CreateTemplateResult.NAME_IS_REQUIRED),
                arguments(IdIsTaken.class, CreateTemplateResult.ID_IS_TAKEN),
                arguments(FileIsExpired.class, CreateTemplateResult.FILE_IS_EXPIRED),
                arguments(CouldNotFindFile.class, CreateTemplateResult.COULD_NOT_FIND_FILE),
                arguments(TemplateIsEmpty.class, CreateTemplateResult.TEMPLATE_IS_EMPTY),
                arguments(CouldNotProcessFile.class, CreateTemplateResult.COULD_NOT_PROCESS_FILE),
                arguments(FileBelongsToAnotherUser.class, CreateTemplateResult.FILE_BELONGS_TO_ANOTHER_USER)
        );
    }

    static Stream<Arguments> createDocumentExceptions() {
        return Stream.of(
                arguments(ValuesAreRequired.class, CreateDocumentResult.VALUES_ARE_REQUIRED),
                arguments(FileIdIsTaken.class, CreateDocumentResult.FILE_ID_IS_TAKEN),
                arguments(CouldNotFindTemplate.class, CreateDocumentResult.COULD_NOT_FIND_TEMPLATE),
                arguments(FileIsExpired.class, CreateDocumentResult.FILE_IS_EXPIRED),
                arguments(CouldNotFindFile.class, CreateDocumentResult.COULD_NOT_FIND_FILE),
                arguments(CouldNotProcessFile.class, CreateDocumentResult.COULD_NOT_PROCESS_FILE),
                arguments(TemplateBelongsToAnotherUser.class, CreateDocumentResult.TEMPLATE_BELONGS_TO_ANOTHER_USER)
        );
    }

    @AfterEach
    void cleanUp() {
        outbox.clear();
    }

    @Test
    void givenCreateTemplateCommand_whenCreateTemplate_thenResultCreatedSent(UUID id, UUID userId, UUID fileId) {
        var createTemplateCommand = new CreateTemplateCommand(
                id.toString(),
                userId.toString(),
                fileId.toString(),
                "name"
        );
        kafkaTemplate.send(createTemplateCommandTopic, id.toString(), createTemplateCommand);

        await().untilAsserted(() ->
                assertThat(
                        outbox.containsMessage(
                                new Message(
                                        id.toString(),
                                        new CreateTemplateResultResponse(
                                                createTemplateCommand,
                                                CreateTemplateResult.CREATED
                                        ),
                                        createTemplateResultResponseTopic
                                )
                        )
                )
                        .isTrue()
        );
    }

    @ParameterizedTest
    @MethodSource("createTemplateExceptions")
    void givenFailedCreateTemplateCommand_whenCreateTemplate_thenFailedResultSent(
            Class<Exception> exceptionType,
            CreateTemplateResult result,
            UUID id,
            UUID userId,
            UUID fileId
    ) throws Exception {
        var createTemplateCommand = new CreateTemplateCommand(
                id.toString(),
                userId.toString(),
                fileId.toString(),
                "name"
        );
        doThrow(exceptionType).when(module).createTemplate(any(), any(), any(), anyString());
        kafkaTemplate.send(createTemplateCommandTopic, id.toString(), createTemplateCommand);

        await().untilAsserted(() ->
                assertThat(
                        outbox.containsMessage(
                                new Message(
                                        id.toString(),
                                        new CreateTemplateResultResponse(createTemplateCommand, result),
                                        createTemplateResultResponseTopic
                                )
                        )
                )
                        .isTrue()
        );
    }

    @Test
    void givenTechnicalFailure_whenCreateTemplate_thenCommandShouldBeRetried(UUID id, UUID userId, UUID fileId)
            throws Exception {
        var createTemplateCommand = new CreateTemplateCommand(
                id.toString(),
                userId.toString(),
                fileId.toString(),
                "name"
        );
        doThrow(IOException.class)
                .doThrow(IOException.class)
                .doNothing()
                .when(module).createTemplate(any(), any(), any(), anyString());
        kafkaTemplate.send(createTemplateCommandTopic, id.toString(), createTemplateCommand);

        await().untilAsserted(() ->
                assertThat(
                        outbox.containsMessage(
                                new Message(
                                        id.toString(),
                                        new CreateTemplateResultResponse(
                                                createTemplateCommand,
                                                CreateTemplateResult.CREATED
                                        ),
                                        createTemplateResultResponseTopic
                                )
                        )
                )
                        .isTrue()
        );
    }

    @Test
    void givenCreateDocumentCommand_whenCreateDocument_thenResultCreatedSent(UUID id, UUID userId, UUID templateId) {
        var createDocumentCommand = new CreateDocumentCommand(
                id.toString(),
                userId.toString(),
                templateId.toString(),
                Map.of("key", "value")
        );
        kafkaTemplate.send(createDocumentCommandTopic, id.toString(), createDocumentCommand);

        await().untilAsserted(() ->
                assertThat(
                        outbox.containsMessage(
                                new Message(
                                        id.toString(),
                                        new CreateDocumentResultResponse(
                                                createDocumentCommand,
                                                CreateDocumentResult.CREATED
                                        ),
                                        createDocumentResultResponseTopic
                                )
                        )
                )
                        .isTrue()
        );
    }

    @ParameterizedTest
    @MethodSource("createDocumentExceptions")
    void givenFailedCreateDocumentCommand_whenCreateDocument_thenFailedResultSent(
            Class<Exception> exceptionType,
            CreateDocumentResult result,
            UUID id,
            UUID userId,
            UUID templateId
    ) throws Exception {
        var createDocumentCommand = new CreateDocumentCommand(
                id.toString(),
                userId.toString(),
                templateId.toString(),
                Map.of("key", "value")
        );
        doThrow(exceptionType).when(module).createDocument(any(), any(), any(), anyMap());
        kafkaTemplate.send(createDocumentCommandTopic, id.toString(), createDocumentCommand);

        await().untilAsserted(() ->
                assertThat(
                        outbox.containsMessage(
                                new Message(
                                        id.toString(),
                                        new CreateDocumentResultResponse(
                                                createDocumentCommand,
                                                result
                                        ),
                                        createDocumentResultResponseTopic
                                )
                        )
                )
                        .isTrue()
        );
    }


    @TestConfiguration
    static class TestConfig {

        @Bean
        Clock clock() {
            return Clock.fixed(Instant.parse("2020-01-01T00:00:00Z"), ZoneOffset.UTC);
        }
    }
}
