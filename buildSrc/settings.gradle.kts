dependencyResolutionManagement {
    versionCatalogs {
        create("libs") {
            library("openapi-generator", "org.openapitools", "openapi-generator-gradle-plugin")
                .version("7.1.0")
            library("avro-tools", "org.apache.avro", "avro-tools").version("1.11.3")
        }
    }
}
