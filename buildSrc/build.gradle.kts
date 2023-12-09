plugins {
    java
    `kotlin-dsl`
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(libs.openapi.generator)
}

gradlePlugin {
    plugins {
        create("generateOpenApi") {
            id = "generate-open-api"
            implementationClass = "GenerateOpenApiPlugin"
        }
    }
}
