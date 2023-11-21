plugins {
    `java-library`
    `jvm-test-suite`
}

dependencies {
    implementation(project(":clerk-write-api"))
    testImplementation(project(":clerk-write-files-in-memory"))
    testImplementation(project(":clerk-write-templates-in-memory"))
    testImplementation(project(":clerk-write-template-engine-freemarker"))
    testImplementation(libs.junit)
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
            useJUnitJupiter(libs.versions.junit)
        }
    }
}
