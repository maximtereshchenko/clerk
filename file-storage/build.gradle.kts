plugins {
    `java-library`
    `test-conventions`
}

dependencies {
    implementation(project(":file-storage-api"))
    testImplementation(testFixtures(project(":test-common")))
    testImplementation(project(":file-storage-files-on-disk"))
    testImplementation(project(":file-storage-file-labels-in-memory"))
    testImplementation(libs.assertj)
}
