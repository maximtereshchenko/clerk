package com.github.maximtereshchenko.gateway;

import com.github.maximtereshchenko.test.ContainerExtension;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolver;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.utility.DockerImageName;
import org.testcontainers.utility.MountableFile;

import java.util.Map;
import java.util.Objects;
import java.util.UUID;

final class KeycloakExtension extends ContainerExtension<GenericContainer<?>> implements ParameterResolver {

    private static final String ISSUER_URI_KEY = "spring.security.oauth2.resourceserver.jwt.issuer-uri";

    private final RestTemplate restTemplate = new RestTemplate();

    KeycloakExtension() {
        super(
                Map.of(
                        ISSUER_URI_KEY,
                        container ->
                                "http://%s:%s/realms/test".formatted(
                                        container.getHost(),
                                        container.getFirstMappedPort()
                                )
                )
        );
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) {
        return parameterContext.getParameter().getType() == User.class;
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) {
        return new User(
                UUID.fromString("00000000-0000-0000-0000-000000000001"),
                Objects.requireNonNull(
                                restTemplate.exchange(
                                                requestEntity(),
                                                new ParameterizedTypeReference<Map<String, String>>() {}
                                        )
                                        .getBody()
                        )
                        .get("access_token")
        );
    }

    @Override
    protected GenericContainer<?> container() {
        return new GenericContainer<>(DockerImageName.parse("keycloak/keycloak:23.0"))
                .withEnv("KEYCLOAK_ADMIN", "admin")
                .withEnv("KEYCLOAK_ADMIN_PASSWORD", "admin")
                .withCommand("start-dev --import-realm")
                .withCopyFileToContainer(
                        MountableFile.forClasspathResource("./realm.json"),
                        "/opt/keycloak/data/import/realm.json"
                )
                .withExposedPorts(8080);
    }

    private RequestEntity<LinkedMultiValueMap<String, String>> requestEntity() {
        return RequestEntity.post(System.getProperty(ISSUER_URI_KEY) + "/protocol/openid-connect/token")
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .body(body());
    }

    private LinkedMultiValueMap<String, String> body() {
        var body = new LinkedMultiValueMap<String, String>();
        body.add("username", "test");
        body.add("password", "test");
        body.add("grant_type", "password");
        body.add("client_id", "test");
        return body;
    }
}
