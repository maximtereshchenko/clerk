import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.SourceSetContainer
import org.gradle.api.tasks.compile.JavaCompile
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.withType

class GenerateOpenApiPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        val extension = target.extensions.create("generateOpenApi", GenerateOpenApiPluginExtension::class.java)
        val task = target.tasks.register("generateOpenApi", GenerateOpenApiTask::class.java) {
            files.set(extension.files)
            packageName.set(extension.packageName)
            output.set("${project.layout.buildDirectory.get()}/generated/sources/open-api/main/java")
        }
        target.tasks.withType<JavaCompile> {
            dependsOn += task
        }
        target.configure<SourceSetContainer> {
            named("main") {
                java {
                    srcDir(task)
                }
            }
        }
    }
}