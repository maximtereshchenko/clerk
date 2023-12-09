plugins {
    java
    `jvm-test-suite`
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
