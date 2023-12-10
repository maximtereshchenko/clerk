plugins {
    `java-library`
    `test-conventions`
}

dependencies {
    implementation(platform(libs.spring.boot.bom))
    implementation(libs.spring.boot.starter.data.jdbc)
    implementation(libs.kafka.clients)
    implementation(libs.kafka.avro.serializer)
    api(libs.avro)
    testImplementation(testFixtures(project(":test-common")))
    testImplementation(libs.spring.boot.starter.test)
    testImplementation(libs.flyway.core)
    testImplementation(libs.postgresql)
}

tasks.compileJava {
    options.compilerArgs.add("-parameters")
}
