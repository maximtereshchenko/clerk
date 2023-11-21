plugins {
    `java-library`
}

dependencies {
    implementation(project(":clerk-write-api"))
    implementation(libs.freemarker)
}
