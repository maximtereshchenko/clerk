plugins {
    `java-library`
    `jvm-test-suite`
}

dependencies {
    implementation(project(":file-storage-api"))
    testImplementation(testFixtures(project(":test-common")))
    testImplementation(project(":file-storage-files-on-disk"))
    testImplementation(project(":file-storage-file-labels-in-memory"))
    testImplementation(libs.junit.jupiter)
    testImplementation(libs.assertj)
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
