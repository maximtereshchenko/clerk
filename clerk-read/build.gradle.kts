plugins {
    `java-library`
    `jvm-test-suite`
}

dependencies {
    implementation(project(":clerk-read-api"))
    testImplementation(project(":clerk-read-templates-in-memory"))
    testImplementation(testFixtures(project(":test-common")))
    testImplementation(libs.junit)
    testImplementation(libs.assertj)
}

testing {
    suites {
        val test by getting(JvmTestSuite::class) {
            useJUnitJupiter(libs.versions.junit)
        }
    }
}

