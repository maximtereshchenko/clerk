package com.github.maximtereshchenko.outbox;

import io.confluent.kafka.serializers.AbstractKafkaSchemaSerDeConfig;
import io.confluent.kafka.serializers.KafkaAvroSerializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories;

import java.time.Clock;
import java.util.Map;

@AutoConfiguration
@EnableJdbcRepositories(basePackageClasses = OutboxRepository.class)
class OutboxAutoConfiguration {

    @Bean
    Outbox outbox(
            @Value("${clerk.schema.registry.url}") String schemaRegistryUrl,
            OutboxRepository outboxRepository,
            Clock clock
    ) {
        var kafkaAvroSerializer = new KafkaAvroSerializer();
        kafkaAvroSerializer.configure(
                Map.of(
                        AbstractKafkaSchemaSerDeConfig.AUTO_REGISTER_SCHEMAS, true,
                        AbstractKafkaSchemaSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG, schemaRegistryUrl
                ),
                false
        );
        return new Outbox(new StringSerializer(), kafkaAvroSerializer, outboxRepository, clock);
    }
}
