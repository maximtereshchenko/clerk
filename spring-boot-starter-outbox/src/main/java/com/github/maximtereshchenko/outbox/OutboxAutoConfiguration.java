package com.github.maximtereshchenko.outbox;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories;

import java.time.Clock;

@AutoConfiguration
@EnableJdbcRepositories(basePackageClasses = OutboxRepository.class)
class OutboxAutoConfiguration {

    @Bean
    Outbox outbox(
            @Value("${clerk.schema.registry.url}") String schemaRegistryUrl,
            OutboxRepository outboxRepository,
            Clock clock
    ) {
        return Outbox.from(schemaRegistryUrl, outboxRepository, clock);
    }
}
