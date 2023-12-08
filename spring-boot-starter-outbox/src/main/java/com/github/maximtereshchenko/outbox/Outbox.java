package com.github.maximtereshchenko.outbox;

import org.apache.avro.generic.GenericRecord;
import org.apache.kafka.common.serialization.Serializer;

import java.time.Clock;

public final class Outbox {

    private final Serializer<String> keySerializer;
    private final Serializer<Object> valueSerializer;
    private final OutboxRepository outboxRepository;
    private final Clock clock;

    Outbox(
            Serializer<String> keySerializer,
            Serializer<Object> valueSerializer,
            OutboxRepository outboxRepository,
            Clock clock
    ) {
        this.keySerializer = keySerializer;
        this.valueSerializer = valueSerializer;
        this.outboxRepository = outboxRepository;
        this.clock = clock;
    }

    public void put(String key, GenericRecord value, String topic) {
        outboxRepository.save(
                new OutboxItem(
                        keySerializer.serialize(topic, key),
                        valueSerializer.serialize(topic, value),
                        topic,
                        clock.instant()
                )
        );
    }

    public long size() {
        return outboxRepository.count();
    }
}
