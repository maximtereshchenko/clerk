plugins {
    `java-test-fixtures`
}

dependencies {
    testFixturesApi(platform(libs.testcontainers.bom))
    testFixturesApi(libs.testcontainers)
    testFixturesApi(libs.testcontainers.jdbc)
    testFixturesApi(libs.testcontainers.kafka)
    testFixturesImplementation(libs.junit.jupiter)
}
