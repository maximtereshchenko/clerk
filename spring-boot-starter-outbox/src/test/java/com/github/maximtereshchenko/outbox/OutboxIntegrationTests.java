package com.github.maximtereshchenko.outbox;

import com.github.maximtereshchenko.test.ClasspathResource;
import com.github.maximtereshchenko.test.ConfluentPlatformExtension;
import com.github.maximtereshchenko.test.PostgreSqlExtension;
import org.apache.avro.Schema;
import org.apache.avro.generic.GenericData;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.NONE,
        properties = "spring.flyway.locations=classpath:outbox/migration"
)
@SpringBootConfiguration
@EnableAutoConfiguration
@ExtendWith({PostgreSqlExtension.class, ConfluentPlatformExtension.class})
class OutboxIntegrationTests {

    @Autowired
    private Outbox outbox;

    @AfterEach
    void cleanUp() {
        outbox.clear();
    }

    @Test
    void givenAvroRecord_whenPut_thenMessageSaved(@ClasspathResource("message.avsc") Path schemaPath) throws Exception {
        var message = new Message("key", new GenericData.Record(schema(schemaPath)), "topic");

        outbox.put(message);

        assertThat(outbox.containsMessage(message)).isTrue();
    }

    @Test
    void givenNoMessages_whenContainsMessage_thenFalseReturned(
            @ClasspathResource("message.avsc") Path schemaPath
    ) throws Exception {
        assertThat(
                outbox.containsMessage(
                        new Message("key", new GenericData.Record(schema(schemaPath)), "topic")
                )
        )
                .isFalse();
    }

    private Schema schema(Path path) throws IOException {
        try (var inputStream = Files.newInputStream(path)) {
            return new Schema.Parser().parse(inputStream);
        }
    }

    @TestConfiguration
    static class TestConfig {

        @Bean
        Clock clock() {
            return Clock.fixed(Instant.parse("2020-01-01T00:00:00Z"), ZoneOffset.UTC);
        }
    }
}