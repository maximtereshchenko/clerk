package com.github.maximtereshchenko.gateway;

import com.github.maximtereshchenko.clerk.write.CreateDocumentCommand;
import com.github.maximtereshchenko.clerk.write.CreateTemplateCommand;
import com.github.maximtereshchenko.outbox.Message;
import com.github.maximtereshchenko.outbox.Outbox;
import com.github.maximtereshchenko.test.ConfluentPlatformExtension;
import com.github.maximtereshchenko.test.PostgreSqlExtension;
import com.github.maximtereshchenko.test.PredictableUUIDExtension;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;

import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = {
                "clerk.write.create-template-command.topic=create-template-command",
                "clerk.write.create-document-command.topic=create-document-command"
        }
)
@ExtendWith({
        KeycloakExtension.class,
        PostgreSqlExtension.class,
        ConfluentPlatformExtension.class,
        PredictableUUIDExtension.class,
        ClerkReadExtension.class
})
final class IntegrationTests {

    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private Outbox outbox;
    @Value("${clerk.write.create-template-command.topic}")
    private String createTemplateCommandTopic;
    @Value("${clerk.write.create-document-command.topic}")
    private String createDocumentCommandTopic;

    @AfterEach
    void cleanUp() {
        outbox.clear();
    }

    @Test
    void givenAuthorizedUser_whenViewTemplates_thenTemplatesRetrieved(User user) {
        var response = restTemplate.exchange(
                "/templates",
                HttpMethod.GET,
                request(user.token()),
                new ParameterizedTypeReference<Collection<ClerkReadGatewayTemplate>>() {}
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody())
                .containsExactly(
                        new ClerkReadGatewayTemplate(
                                user.id(),
                                user.id(),
                                "name",
                                Instant.parse("2020-01-01T00:00:00Z")
                        )
                );
    }

    @Test
    void givenAuthorizedUser_whenViewPlaceholders_thenPlaceholdersRetrieved(User user) {
        var response = restTemplate.exchange(
                "/templates/00000000-0000-0000-0000-000000000001/placeholders",
                HttpMethod.GET,
                request(user.token()),
                ClerkReadGatewayPlaceholders.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody())
                .isEqualTo(
                        new ClerkReadGatewayPlaceholders(
                                UUID.fromString("00000000-0000-0000-0000-000000000001"),
                                user.id(),
                                List.of("key"),
                                Instant.parse("2020-01-01T00:00:00Z")
                        )
                );
    }

    @Test
    void givenAuthorizedUser_whenViewDocuments_thenDocumentsRetrieved(User user) {
        var response = restTemplate.exchange(
                "/documents",
                HttpMethod.GET,
                request(user.token()),
                new ParameterizedTypeReference<Collection<ClerkReadGatewayDocument>>() {}
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        var instant = Instant.parse("2020-01-01T00:00:00Z");
        assertThat(response.getBody())
                .containsExactly(new ClerkReadGatewayDocument(user.id(), user.id(), instant, instant));
    }

    @Test
    void givenAuthorizedUser_whenCreateTemplate_thenCreateTemplateCommandSent(User user, UUID id, UUID fileId) {
        var response = restTemplate.exchange(
                "/templates",
                HttpMethod.POST,
                request(
                        user.token(),
                        new ClerkWriteGatewayCreateTemplateRequest(
                                id,
                                fileId,
                                "name"
                        )
                ),
                Void.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.ACCEPTED);
        assertThat(
                outbox.containsMessage(
                        new Message(
                                id.toString(),
                                new CreateTemplateCommand(
                                        id.toString(),
                                        user.id().toString(),
                                        fileId.toString(),
                                        "name"
                                ),
                                createTemplateCommandTopic
                        )
                )
        )
                .isTrue();
    }

    @Test
    void givenAuthorizedUser_whenCreateTemplate_thenCreateDocumentCommandSent(User user, UUID id, UUID templateId) {
        var response = restTemplate.exchange(
                "/documents",
                HttpMethod.POST,
                request(
                        user.token(),
                        new ClerkWriteGatewayCreateDocumentRequest(
                                id,
                                templateId,
                                List.of(new ClerkWriteGatewayCreateDocumentRequestValuesInner("key", "value"))
                        )
                ),
                Void.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.ACCEPTED);
        assertThat(
                outbox.containsMessage(
                        new Message(
                                id.toString(),
                                new CreateDocumentCommand(
                                        id.toString(),
                                        user.id().toString(),
                                        templateId.toString(),
                                        Map.of("key", "value")
                                ),
                                createDocumentCommandTopic
                        )
                )
        )
                .isTrue();
    }

    private <T> HttpEntity<T> request(String token) {
        return request(token, null);
    }

    private <T> HttpEntity<T> request(String token, T body) {
        var httpHeaders = new HttpHeaders();
        httpHeaders.setBearerAuth(token);
        return new HttpEntity<>(body, httpHeaders);
    }
}