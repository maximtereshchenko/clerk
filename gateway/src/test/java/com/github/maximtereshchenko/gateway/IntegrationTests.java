package com.github.maximtereshchenko.gateway;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
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
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith({KeycloakExtension.class, ClerkReadExtension.class})
final class IntegrationTests {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void givenAuthorizedUser_whenViewTemplates_thenTemplatesRetrieved(String token, UUID userId) {
        var response = restTemplate.exchange(
                "/templates",
                HttpMethod.GET,
                authorization(token),
                new ParameterizedTypeReference<Collection<ClerkReadGatewayTemplate>>() {}
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody())
                .containsExactly(
                        new ClerkReadGatewayTemplate()
                                .id(userId)
                                .userId(userId)
                                .name("name")
                                .timestamp(Instant.parse("2020-01-01T00:00:00Z"))
                );
    }

    @Test
    void givenAuthorizedUser_whenViewPlaceholders_thenPlaceholdersRetrieved(String token, UUID userId) {
        var response = restTemplate.exchange(
                "/templates/00000000-0000-0000-0000-000000000001/placeholders",
                HttpMethod.GET,
                authorization(token),
                ClerkReadGatewayPlaceholders.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody())
                .isEqualTo(
                        new ClerkReadGatewayPlaceholders()
                                .id(UUID.fromString("00000000-0000-0000-0000-000000000001"))
                                .userId(userId)
                                .placeholders(List.of("key"))
                                .timestamp(Instant.parse("2020-01-01T00:00:00Z"))
                );
    }

    private <T> HttpEntity<T> authorization(String token) {
        var httpHeaders = new HttpHeaders();
        httpHeaders.setBearerAuth(token);
        return new HttpEntity<>(httpHeaders);
    }
}