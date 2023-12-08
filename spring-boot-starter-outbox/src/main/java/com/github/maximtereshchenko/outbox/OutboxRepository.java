package com.github.maximtereshchenko.outbox;

import org.springframework.data.repository.ListCrudRepository;

interface OutboxRepository extends ListCrudRepository<OutboxItem, Long> {}
