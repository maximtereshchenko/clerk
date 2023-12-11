package com.github.maximtereshchenko.outbox;

import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;

interface OutboxRepository extends ListCrudRepository<OutboxItem, Long>, QueryByExampleExecutor<OutboxItem> {}
