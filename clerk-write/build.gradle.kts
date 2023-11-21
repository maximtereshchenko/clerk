plugins {
    `java-library`
    `jvm-test-suite`
}

dependencies {
    implementation(project(":clerk-write-api"))
    testImplementation(project(":files-in-memory"))
    testImplementation(project(":templates-in-memory"))
    testImplementation(project(":template-engine-freemarker"))
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
