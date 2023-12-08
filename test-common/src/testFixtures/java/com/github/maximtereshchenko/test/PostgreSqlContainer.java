package com.github.maximtereshchenko.test;

import org.testcontainers.containers.JdbcDatabaseContainer;
import org.testcontainers.utility.DockerImageName;

public final class PostgreSqlContainer extends JdbcDatabaseContainer<PostgreSqlContainer> {

    private static final String DATABASE_NAME = "test";
    private static final int PORT = 5432;

    public PostgreSqlContainer(DockerImageName dockerImageName) {
        super(dockerImageName);
        addEnv("POSTGRES_DB", DATABASE_NAME);
        addEnv("POSTGRES_USER", getUsername());
        addEnv("POSTGRES_PASSWORD", getPassword());
        addExposedPort(PORT);
    }

    @Override
    public String getDriverClassName() {
        return "org.postgresql.Driver";
    }

    @Override
    public String getJdbcUrl() {
        return "jdbc:postgresql://%s:%d/%s%s".formatted(
                getHost(),
                getMappedPort(PORT),
                DATABASE_NAME,
                constructUrlParameters("?", "&")
        );
    }

    @Override
    public String getUsername() {
        return "test";
    }

    @Override
    public String getPassword() {
        return "test";
    }

    @Override
    protected String getTestQueryString() {
        return "SELECT 1";
    }
}
