plugins {
    application
    `generate-open-api`
    `generate-avro`
    `test-conventions`
}

dependencies {
    implementation(project(":spring-boot-starter-outbox"))
    implementation(platform(libs.spring.boot.bom))
    implementation(platform(libs.spring.cloud.bom))
    implementation(libs.spring.boot.starter.web)
    implementation(libs.spring.boot.starter.validation)
    implementation(libs.spring.boot.starter.actuator)
    implementation(libs.spring.boot.starter.security)
    implementation(libs.spring.boot.starter.oauth2.resource.server)
    implementation(libs.spring.boot.starter.data.jdbc)
    implementation(libs.spring.cloud.starter.openfeign)
    implementation(libs.mapstruct)
    implementation(libs.flyway.core)
    implementation(libs.postgresql)
    annotationProcessor(libs.mapstruct.processor)
    testImplementation(testFixtures(project(":test-common")))
    testImplementation(libs.assertj)
    testImplementation(libs.spring.boot.starter.test)
    testImplementation(libs.wiremock)
}

generateOpenApi {
    files = listOf(
        "${rootDir}/open-api-specifications/clerk-read-api.json",
        "${rootDir}/open-api-specifications/gateway-api.json"
    )
    packageName = "com.github.maximtereshchenko.gateway"
}

generateAvro {
    files = listOf("${rootDir}/avro/create-template-command.avsc")
}
