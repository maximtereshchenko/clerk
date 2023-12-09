dependencyResolutionManagement {
    versionCatalogs {
        create("libs") {
            library("openapi-generator", "org.openapitools", "openapi-generator-gradle-plugin")
                .version("7.1.0")
        }
    }
}
