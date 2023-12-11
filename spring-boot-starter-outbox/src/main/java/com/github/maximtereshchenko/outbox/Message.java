package com.github.maximtereshchenko.outbox;

import org.apache.avro.generic.GenericRecord;

public record Message(String key, GenericRecord value, String topic) {}
