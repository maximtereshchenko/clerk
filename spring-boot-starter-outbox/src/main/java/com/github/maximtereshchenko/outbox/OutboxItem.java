package com.github.maximtereshchenko.outbox;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.Instant;

@Table("outbox")
public final class OutboxItem {

    @Id
    private Long id;
    private byte[] messageKey;
    private byte[] messagePayload;
    private String topic;
    private Instant createdAt;

    private OutboxItem() {
    }

    OutboxItem(byte[] messageKey, byte[] messagePayload, String topic, Instant createdAt) {
        this.messageKey = messageKey.clone();
        this.messagePayload = messagePayload.clone();
        this.topic = topic;
        this.createdAt = createdAt;
    }

    public Long id() {
        return id;
    }

    public byte[] messageKey() {
        return messageKey;
    }

    public byte[] messagePayload() {
        return messagePayload;
    }

    public String topic() {
        return topic;
    }

    public Instant createdAt() {
        return createdAt;
    }
}
