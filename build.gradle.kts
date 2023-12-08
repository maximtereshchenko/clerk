import java.net.URI

allprojects {
    group = "com.github.maximtereshchenko"
    version = "1.0.0"
}

subprojects {
    repositories {
        mavenCentral()
        maven {
            url = URI.create("https://packages.confluent.io/maven")
        }
    }
}
