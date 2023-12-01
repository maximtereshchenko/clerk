package com.github.maximtereshchenko.gateway;

import org.junit.jupiter.api.extension.*;
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

final class KeycloakExtension implements BeforeAllCallback, AfterAllCallback, ParameterResolver {

    private static final String ISSUER_URI_KEY = "spring.security.oauth2.resourceserver.jwt.issuer-uri";

    private final ExtensionContext.Namespace namespace = ExtensionContext.Namespace.create(getClass());
    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public void beforeAll(ExtensionContext context) {
        var keycloak = new GenericContainer<>(DockerImageName.parse("keycloak/keycloak:23.0"))
                .withEnv("KEYCLOAK_ADMIN", "admin")
                .withEnv("KEYCLOAK_ADMIN_PASSWORD", "admin")
                .withCommand("start-dev --import-realm")
                .withCopyFileToContainer(
                        MountableFile.forClasspathResource("./realm.json"),
                        "/opt/keycloak/data/import/realm.json"
                )
                .withExposedPorts(8080);
        keycloak.start();
        System.setProperty(
                ISSUER_URI_KEY,
                "http://%s:%s/realms/test".formatted(keycloak.getHost(), keycloak.getFirstMappedPort())
        );
        context.getStore(namespace)
                .put(GenericContainer.class, keycloak);
    }

    @Override
    public void afterAll(ExtensionContext context) {
        context.getStore(namespace)
                .get(GenericContainer.class, GenericContainer.class)
                .stop();
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) {
        var type = parameterContext.getParameter().getType();
        return type == String.class || type == UUID.class;
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) {
        if (parameterContext.getParameter().getType() == UUID.class) {
            return UUID.fromString("00000000-0000-0000-0000-000000000001");
        }
        return Objects.requireNonNull(
                        restTemplate.exchange(
                                        requestEntity(),
                                        new ParameterizedTypeReference<Map<String, String>>() {}
                                )
                                .getBody()
                )
                .get("access_token");
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
