rootProject.name = "clerk"
include("clerk-write-api")
include("clerk-write")
include("clerk-write-templates-in-memory")
include("clerk-write-template-engine-freemarker")
include("clerk-read-api")
include("clerk-events")
include("clerk-read")
include("test-common")
include("clerk-read-templates-in-memory")
include("clerk-read-placeholders-in-memory")
include("clerk-read-documents-in-memory")
include("file-storage")
include("file-storage-api")
include("file-storage-files-on-disk")
include("file-storage-file-labels-in-memory")
include("gateway")
include("spring-boot-starter-outbox")
dependencyResolutionManagement {
    versionCatalogs {
        create("libs") {
            val mapstruct = version("mapstruct", "1.5.5.Final")
            library("testcontainers-bom", "org.testcontainers", "testcontainers-bom")
                .version("1.19.3")
            library("spring-boot-bom", "org.springframework.boot", "spring-boot-dependencies")
                .version("3.1.6")
            library("spring-cloud-bom", "org.springframework.cloud", "spring-cloud-dependencies")
                .version("2022.0.4")
            library("assertj", "org.assertj", "assertj-core").version("3.24.2")
            library("freemarker", "org.freemarker", "freemarker").version("2.3.32")
            library("junit-jupiter", "org.junit.jupiter", "junit-jupiter").version("5.10.1")
            library("wiremock", "org.wiremock", "wiremock").version("3.3.1")
            library("flyway-core", "org.flywaydb", "flyway-core").version("9.16.3")
            library("postgresql", "org.postgresql", "postgresql").version("42.7.0")
            library("kafka-avro-serializer", "io.confluent", "kafka-avro-serializer").version("7.5.1")
            library("avro-tools", "org.apache.avro", "avro-tools").version("1.11.3")
            library("mapstruct", "org.mapstruct", "mapstruct").versionRef(mapstruct)
            library("mapstruct-processor", "org.mapstruct", "mapstruct-processor").versionRef(mapstruct)
            library("testcontainers", "org.testcontainers", "testcontainers").withoutVersion()
            library("testcontainers-jdbc", "org.testcontainers", "jdbc").withoutVersion()
            library("testcontainers-kafka", "org.testcontainers", "kafka").withoutVersion()
            library("kafka-clients", "org.apache.kafka", "kafka-clients").withoutVersion()
            library("spring-boot-starter-web", "org.springframework.boot", "spring-boot-starter-web")
                .withoutVersion()
            library(
                "spring-boot-starter-validation",
                "org.springframework.boot",
                "spring-boot-starter-validation"
            )
                .withoutVersion()
            library(
                "spring-boot-starter-test",
                "org.springframework.boot",
                "spring-boot-starter-test"
            )
                .withoutVersion()
            library(
                "spring-boot-starter-actuator",
                "org.springframework.boot",
                "spring-boot-starter-actuator"
            )
                .withoutVersion()
            library(
                "spring-boot-starter-security",
                "org.springframework.boot",
                "spring-boot-starter-security"
            )
                .withoutVersion()
            library(
                "spring-boot-starter-oauth2-resource-server",
                "org.springframework.boot",
                "spring-boot-starter-oauth2-resource-server"
            )
                .withoutVersion()
            library(
                "spring-boot-starter-data-jdbc",
                "org.springframework.boot",
                "spring-boot-starter-data-jdbc"
            )
                .withoutVersion()
            library(
                "spring-cloud-starter-openfeign",
                "org.springframework.cloud",
                "spring-cloud-starter-openfeign"
            )
                .withoutVersion()
            plugin("openapi-generator", "org.openapi.generator").version("7.1.0")
        }
    }
}
