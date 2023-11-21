rootProject.name = "clerk"
include("clerk-write-api")
include("clerk-write")
include("files-in-memory")
dependencyResolutionManagement {
    versionCatalogs {
        create("libs") {
            val junit = version("junit", "5.10.0")
            library("junit", "org.junit.jupiter", "junit-jupiter")
                .versionRef(junit)
            library("junit-engine", "org.junit.jupiter", "junit-jupiter-engine")
                .versionRef(junit)
            library("assertj", "org.assertj:assertj-core:3.24.2")
        }
    }
}
