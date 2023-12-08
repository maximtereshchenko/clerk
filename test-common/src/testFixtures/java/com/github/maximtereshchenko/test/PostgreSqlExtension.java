package com.github.maximtereshchenko.test;

import org.testcontainers.containers.JdbcDatabaseContainer;
import org.testcontainers.utility.DockerImageName;

import java.util.Map;

public final class PostgreSqlExtension extends ContainerExtension<PostgreSqlContainer> {

    PostgreSqlExtension() {
        super(
                Map.of(
                        "spring.datasource.url", JdbcDatabaseContainer::getJdbcUrl,
                        "spring.datasource.username", JdbcDatabaseContainer::getUsername,
                        "spring.datasource.password", JdbcDatabaseContainer::getPassword
                )
        );
    }

    @Override
    protected PostgreSqlContainer container() {
        return new PostgreSqlContainer(DockerImageName.parse("postgres:16.1"));
    }
}
