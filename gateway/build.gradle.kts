import org.apache.avro.tool.SpecificCompilerTool

plugins {
    application
    //alias(libs.plugins.openapi.generator)
    `generate-open-api`
}

buildscript {
    dependencies {
        classpath(libs.avro.tools)
    }
}

dependencies {
    implementation(project(":spring-boot-starter-outbox"))
    implementation(platform(libs.spring.boot.bom))
    implementation(platform(libs.spring.cloud.bom))
    implementation(libs.spring.boot.starter.web)
    implementation(libs.spring.boot.starter.validation)
    implementation(libs.spring.boot.starter.actuator)
    implementation(libs.spring.boot.starter.security)
    implementation(libs.spring.boot.starter.oauth2.resource.server)
    implementation(libs.spring.boot.starter.data.jdbc)
    implementation(libs.spring.cloud.starter.openfeign)
    implementation(libs.mapstruct)
    implementation(libs.flyway.core)
    implementation(libs.postgresql)
    annotationProcessor(libs.mapstruct.processor)
    testImplementation(testFixtures(project(":test-common")))
    testImplementation(libs.assertj)
    testImplementation(libs.spring.boot.starter.test)
    testImplementation(libs.wiremock)
}

generateOpenApi {
    files = listOf(
        "${rootDir}/open-api-specifications/clerk-read-api.json",
        "${rootDir}/open-api-specifications/gateway-api.json"
    )
    packageName = "com.github.maximtereshchenko.gateway"
}

val generateAvro = tasks.register<DefaultTask>("generate-avro") {
    val schemas = layout.projectDirectory.dir("src/main/avro")
    val output = layout.buildDirectory.dir("generated/sources/avro/main/java").get()
    inputs.dir(schemas)
    outputs.dir(output)
    logging.captureStandardOutput(LogLevel.INFO);
    logging.captureStandardError(LogLevel.ERROR)
    doLast {
        SpecificCompilerTool().run(
            System.`in`,
            System.out,
            System.err,
            listOf(
                "-encoding", "UTF-8",
                "-string",
                "-fieldVisibility", "private",
                "-noSetters",
                "schema", schemas.toString(),
                output.toString()
            )
        )
    }
}

sourceSets {
    main {
        java {
            srcDir(generateAvro)
        }
    }
}

tasks {
    compileJava {
        dependsOn += generateAvro
    }
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
