plugins {
    `java-library`
    `jvm-test-suite`
}

dependencies {
    implementation(project(":clerk-write-api"))
    testImplementation(project(":clerk-write-templates-in-memory"))
    testImplementation(project(":clerk-write-template-engine-freemarker"))
    testImplementation(testFixtures(project(":test-common")))
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
