plugins {
    `kotlin-dsl`
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(libs.openapi.generator)
    implementation(libs.avro.tools)
}

gradlePlugin {
    plugins {
        create("generateOpenApi") {
            id = "generate-open-api"
            implementationClass = "GenerateOpenApiPlugin"
        }
        create("generateAvro") {
            id = "generate-avro"
            implementationClass = "GenerateAvroPlugin"
        }
    }
}
