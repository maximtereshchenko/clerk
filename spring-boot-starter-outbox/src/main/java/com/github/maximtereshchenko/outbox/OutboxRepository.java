package com.github.maximtereshchenko.outbox;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.ListCrudRepository;

interface OutboxRepository extends ListCrudRepository<OutboxItem, Long> {

    @Query("SELECT COUNT(1) FROM outbox WHERE message_key = :messageKey")
    boolean existsByMessageKey(byte[] messageKey);
}
