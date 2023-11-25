rootProject.name = "clerk"
include("clerk-write-api")
include("clerk-write")
include("clerk-write-files-in-memory")
include("clerk-write-templates-in-memory")
include("clerk-write-template-engine-freemarker")
include("clerk-read-api")
include("clerk-events")
include("clerk-read")
include("test-common")
include("clerk-read-templates-in-memory")
include("clerk-read-placeholders-in-memory")
include("clerk-read-documents-in-memory")
dependencyResolutionManagement {
    versionCatalogs {
        create("libs") {
            val junit = version("junit", "5.10.0")
            library("junit", "org.junit.jupiter", "junit-jupiter")
                .versionRef(junit)
            library("junit-engine", "org.junit.jupiter", "junit-jupiter-engine")
                .versionRef(junit)
            library("assertj", "org.assertj:assertj-core:3.24.2")
            library("freemarker", "org.freemarker:freemarker:2.3.32")
        }
    }
}
