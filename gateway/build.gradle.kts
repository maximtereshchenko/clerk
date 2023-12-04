import org.openapitools.generator.gradle.plugin.tasks.GenerateTask
import java.nio.file.Paths
import kotlin.io.path.listDirectoryEntries

plugins {
    application
    alias(libs.plugins.openapi.generator)
}

dependencies {
    implementation(platform(libs.spring.boot.bom))
    implementation(platform(libs.spring.cloud.bom))
    implementation(libs.spring.boot.starter.web)
    implementation(libs.spring.boot.starter.validation)
    implementation(libs.spring.boot.starter.actuator)
    implementation(libs.spring.boot.starter.security)
    implementation(libs.spring.boot.starter.oauth2.resource.server)
    implementation(libs.spring.cloud.starter.openfeign)
    implementation(libs.mapstruct)
    annotationProcessor(libs.mapstruct.processor)
    testImplementation(platform(libs.testcontainers.bom))
    testImplementation(libs.junit.jupiter)
    testImplementation(libs.assertj)
    testImplementation(libs.testcontainers)
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
            dependsOn += task.name
        }
        sourceSets {
            main {
                java {
                    srcDir(task)
                }
            }
        }
    }

testing {
    suites {
        val test by getting(JvmTestSuite::class) {
            useJUnitJupiter()
        }
    }
}
