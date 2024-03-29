plugins {
    application
    `generate-avro`
    `test-conventions`
}

dependencies {
    implementation(project(":clerk-write-api"))
    implementation(project(":spring-boot-starter-outbox"))
    implementation(platform(libs.spring.boot.bom))
    implementation(libs.spring.boot.autoconfigure)
    implementation(libs.spring.kafka)
    testImplementation(testFixtures(project(":test-common")))
    testImplementation(libs.assertj)
    testImplementation(libs.awaitility)
    testImplementation(libs.spring.boot.starter.test)
    testImplementation(libs.flyway.core)
}

generateAvro {
    files = listOf(
        "${rootDir}/avro/create-template-command.avsc",
        "${rootDir}/avro/create-template-result-response.avsc",
        "${rootDir}/avro/create-document-command.avsc",
        "${rootDir}/avro/create-document-result-response.avsc"
    )
}
