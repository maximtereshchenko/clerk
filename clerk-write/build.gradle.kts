plugins {
    `java-library`
    `test-conventions`
}

dependencies {
    api(project(":clerk-write-api"))
    testImplementation(project(":clerk-write-templates-in-memory"))
    testImplementation(project(":clerk-write-template-engine-freemarker"))
    testImplementation(testFixtures(project(":test-common")))
    testImplementation(libs.assertj)
}
