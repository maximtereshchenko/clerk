package com.github.maximtereshchenko.outbox;

import io.confluent.kafka.schemaregistry.avro.AvroSchemaProvider;
import io.confluent.kafka.schemaregistry.client.SchemaRegistryClient;
import io.confluent.kafka.schemaregistry.client.SchemaRegistryClientFactory;
import io.confluent.kafka.serializers.AbstractKafkaSchemaSerDeConfig;
import io.confluent.kafka.serializers.KafkaAvroSerializer;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Serializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Example;

import java.time.Clock;
import java.util.List;
import java.util.Map;

public final class Outbox {

    private static final Logger LOG = LoggerFactory.getLogger(Outbox.class);

    private final Serializer<String> keySerializer;
    private final String schemaRegistryUrl;
    private final SchemaRegistryClient schemaRegistryClient;
    private final OutboxRepository outboxRepository;
    private final Clock clock;

    private Outbox(
            Serializer<String> keySerializer,
            String schemaRegistryUrl,
            SchemaRegistryClient schemaRegistryClient,
            OutboxRepository outboxRepository,
            Clock clock
    ) {
        this.keySerializer = keySerializer;
        this.schemaRegistryUrl = schemaRegistryUrl;
        this.schemaRegistryClient = schemaRegistryClient;
        this.outboxRepository = outboxRepository;
        this.clock = clock;
    }

    static Outbox from(String schemaRegistryUrl, OutboxRepository outboxRepository, Clock clock) {
        return new Outbox(
                new StringSerializer(),
                schemaRegistryUrl,
                SchemaRegistryClientFactory.newClient(
                        List.of(schemaRegistryUrl),
                        AbstractKafkaSchemaSerDeConfig.MAX_SCHEMAS_PER_SUBJECT_DEFAULT,
                        List.of(new AvroSchemaProvider()),
                        Map.of(),
                        Map.of()
                ),
                outboxRepository,
                clock
        );
    }


    public void put(Message message) {
        outboxRepository.save(outboxItem(message, true).withTimestamp(clock.instant()));
    }

    public boolean containsMessage(Message message) {
        try {
            return outboxRepository.exists(Example.of(outboxItem(message, false)));
        } catch (SerializationException e) {
            LOG.warn("Could not serialize value", e);
            return false;
        }
    }

    public void clear() {
        outboxRepository.deleteAll();
    }

    private OutboxItem outboxItem(Message message, boolean autoRegisterSchemas) {
        try (var valueSerializer = valueSerializer(autoRegisterSchemas)) {
            return new OutboxItem(
                    keySerializer.serialize(message.topic(), message.key()),
                    valueSerializer.serialize(message.topic(), message.value()),
                    message.topic()
            );
        }
    }

    private Serializer<Object> valueSerializer(boolean autoRegisterSchemas) {
        return new KafkaAvroSerializer(
                schemaRegistryClient,
                Map.of(
                        AbstractKafkaSchemaSerDeConfig.AUTO_REGISTER_SCHEMAS, autoRegisterSchemas,
                        AbstractKafkaSchemaSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG, schemaRegistryUrl
                )
        );
    }
}
