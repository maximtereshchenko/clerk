package com.github.maximtereshchenko.test;

import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.containers.Network;
import org.testcontainers.utility.DockerImageName;

import java.util.Collection;
import java.util.List;

public final class ConfluentPlatformExtension implements BeforeAllCallback {

    private static final String VERSION = "7.4.3";
    private final ExtensionContext.Namespace namespace = ExtensionContext.Namespace.create(getClass());

    @Override
    public void beforeAll(ExtensionContext context) {
        var network = Network.newNetwork();
        var kafka = new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka").withTag(VERSION))
                .withNetwork(network)
                .withNetworkAliases("kafka");
        kafka.start();
        var schemaRegistry = new GenericContainer<>(
                DockerImageName.parse("confluentinc/cp-schema-registry").withTag(VERSION)
        )
                .withEnv("SCHEMA_REGISTRY_KAFKASTORE_BOOTSTRAP_SERVERS", "kafka:9092")
                .withEnv("SCHEMA_REGISTRY_HOST_NAME", "localhost")
                .withExposedPorts(8081)
                .withNetwork(network)
                .dependsOn(kafka);
        schemaRegistry.start();
        System.setProperty(
                "clerk.schema.registry.url",
                "http://%s:%d".formatted(schemaRegistry.getHost(), schemaRegistry.getFirstMappedPort())
        );
        context.getStore(namespace).put(Containers.class, new Containers(List.of(kafka, schemaRegistry)));
    }

    private record Containers(Collection<? extends GenericContainer<?>> all)
            implements ExtensionContext.Store.CloseableResource {

        @Override
        public void close() {
            all.forEach(GenericContainer::stop);
        }
    }
}
