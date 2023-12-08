plugins {
    `java-library`
}

dependencies {
    implementation(platform(libs.spring.boot.bom))
    implementation(libs.spring.boot.starter.data.jdbc)
    implementation(libs.kafka.clients)
    api(libs.kafka.avro.serializer)
    testImplementation(testFixtures(project(":test-common")))
    testImplementation(libs.spring.boot.starter.test)
    testImplementation(libs.flyway.core)
    testImplementation(libs.postgresql)
}

tasks {
    compileTestJava {
        options.compilerArgs.add("-parameters")
    }
}

testing {
    suites {
        val test by getting(JvmTestSuite::class) {
            useJUnitJupiter()
        }
    }
}
