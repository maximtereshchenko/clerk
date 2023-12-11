package com.github.maximtereshchenko.clerk.write.application;

import com.github.maximtereshchenko.clerk.write.CreateTemplateCommand;
import com.github.maximtereshchenko.clerk.write.CreateTemplateResult;
import com.github.maximtereshchenko.clerk.write.Result;
import com.github.maximtereshchenko.clerk.write.api.ClerkWriteModule;
import com.github.maximtereshchenko.clerk.write.api.exception.IdIsTaken;
import com.github.maximtereshchenko.clerk.write.api.exception.NameIsRequired;
import com.github.maximtereshchenko.clerk.write.api.exception.TemplateIsEmpty;
import com.github.maximtereshchenko.clerk.write.api.port.exception.CouldNotFindFile;
import com.github.maximtereshchenko.clerk.write.api.port.exception.CouldNotProcessFile;
import com.github.maximtereshchenko.clerk.write.api.port.exception.FileBelongsToAnotherUser;
import com.github.maximtereshchenko.clerk.write.api.port.exception.FileIsExpired;
import com.github.maximtereshchenko.outbox.Message;
import com.github.maximtereshchenko.outbox.Outbox;
import com.github.maximtereshchenko.test.ConfluentPlatformExtension;
import com.github.maximtereshchenko.test.PostgreSqlExtension;
import com.github.maximtereshchenko.test.PredictableUUIDExtension;
import org.apache.avro.generic.GenericRecord;
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

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.UUID;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.NONE,
        properties = {
                "clerk.write.create-template-command.topic=create-template-command",
                "clerk.write.create-template-result.topic=create-template-result",
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
    @Value("${clerk.write.create-template-result.topic}")
    private String createTemplateResultTopic;

    static Stream<Arguments> factory() {
        return Stream.of(
                arguments(NameIsRequired.class, Result.NAME_IS_REQUIRED),
                arguments(IdIsTaken.class, Result.ID_IS_TAKEN),
                arguments(FileIsExpired.class, Result.FILE_IS_EXPIRED),
                arguments(CouldNotFindFile.class, Result.COULD_NOT_FIND_FILE),
                arguments(TemplateIsEmpty.class, Result.TEMPLATE_IS_EMPTY),
                arguments(CouldNotProcessFile.class, Result.COULD_NOT_PROCESS_FILE),
                arguments(FileBelongsToAnotherUser.class, Result.FILE_BELONGS_TO_ANOTHER_USER)
        );
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
                                        new CreateTemplateResult(createTemplateCommand, Result.CREATED),
                                        createTemplateResultTopic
                                )
                        )
                )
                        .isTrue()
        );
    }

    @ParameterizedTest
    @MethodSource("factory")
    void givenFailedCreateTemplateCommand_whenCreateTemplate_thenFailedResultSent(
            Class<Exception> exceptionType,
            Result result,
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
                                        new CreateTemplateResult(createTemplateCommand, result),
                                        createTemplateResultTopic
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
