import org.apache.avro.tool.SpecificCompilerTool
import org.openapitools.generator.gradle.plugin.tasks.GenerateTask
import java.nio.file.Paths
import kotlin.io.path.listDirectoryEntries

plugins {
    application
    alias(libs.plugins.openapi.generator)
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

Paths.get("${project.parent?.layout?.projectDirectory}")
    .resolve("open-api-specifications")
    .listDirectoryEntries()
    .forEach {
        val task = tasks.register<GenerateTask>(it.fileName.toString()) {
            generatorName = "spring"
            inputSpec = it.toString()
            outputDir = "${layout.buildDirectory.get()}/generated/sources/open-api/main/java"
            configOptions = mapOf(
                "apiPackage" to "com.github.maximtereshchenko.gateway",
                "modelPackage" to "com.github.maximtereshchenko.gateway",
                "interfaceOnly" to "true",
                "skipDefaultInterface" to "true",
                "sourceFolder" to "",
                "annotationLibrary" to "none",
                "documentationProvider" to "none",
                "useSpringBoot3" to "true",
                "openApiNullable" to "false",
                "useTags" to "true"
            )
            typeMappings = mapOf(
                "DateTime" to "Instant"
            )
            importMappings = mapOf(
                "Instant" to "java.time.Instant"
            )
        }
        tasks.compileJava {
            dependsOn += task
        }
        sourceSets {
            main {
                java {
                    srcDir(task)
                }
            }
        }
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
