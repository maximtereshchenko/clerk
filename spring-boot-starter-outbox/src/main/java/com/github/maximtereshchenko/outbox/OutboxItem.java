package com.github.maximtereshchenko.outbox;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.Instant;

@Table("outbox")
final class OutboxItem {

    @Id
    private Long id;
    private byte[] messageKey;
    private byte[] messagePayload;
    private String topic;
    private Instant createdAt;

    private OutboxItem() {
    }

    OutboxItem(byte[] messageKey, byte[] messagePayload, String topic) {
        this.messageKey = messageKey.clone();
        this.messagePayload = messagePayload.clone();
        this.topic = topic;
    }

    OutboxItem withTimestamp(Instant instant) {
        createdAt = instant;
        return this;
    }
}
