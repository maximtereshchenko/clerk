package com.github.maximtereshchenko.clerk.write.kafka;

import com.github.maximtereshchenko.clerk.write.api.CreateDocumentFromTemplateUseCase;
import com.github.maximtereshchenko.clerk.write.api.CreateTemplateUseCase;
import com.github.maximtereshchenko.outbox.Outbox;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.EnableKafka;

@AutoConfiguration
@EnableKafka
class ClerkWriteKafkaAutoConfiguration {

    @Bean
    CreateTemplateCommandKafkaConsumer createTemplateCommandKafkaConsumer(
            CreateTemplateUseCase useCase,
            Outbox outbox,
            @Value("${clerk.write.create-template-result-response.topic}") String responseTopic
    ) {
        return new CreateTemplateCommandKafkaConsumer(useCase, outbox, responseTopic);
    }

    @Bean
    CreateDocumentCommandKafkaConsumer createDocumentCommandKafkaConsumer(
            CreateDocumentFromTemplateUseCase useCase,
            Outbox outbox,
            @Value("${clerk.write.create-document-result-response.topic}") String responseTopic
    ) {
        return new CreateDocumentCommandKafkaConsumer(useCase, outbox, responseTopic);
    }
}
