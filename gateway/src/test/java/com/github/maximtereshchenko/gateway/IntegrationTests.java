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

    private <T> HttpEntity<T> authorization(String token) {
        var httpHeaders = new HttpHeaders();
        httpHeaders.setBearerAuth(token);
        return new HttpEntity<>(httpHeaders);
    }
}