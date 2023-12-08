plugins {
    `java-library`
    `jvm-test-suite`
}

dependencies {
    implementation(project(":clerk-read-api"))
    testImplementation(project(":clerk-read-templates-in-memory"))
    testImplementation(project(":clerk-read-placeholders-in-memory"))
    testImplementation(project(":clerk-read-documents-in-memory"))
    testImplementation(testFixtures(project(":test-common")))
    testImplementation(libs.assertj)
}

testing {
    suites {
        val test by getting(JvmTestSuite::class) {
            useJUnitJupiter()
        }
    }
}

