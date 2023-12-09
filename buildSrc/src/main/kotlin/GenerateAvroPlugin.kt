import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.SourceSetContainer
import org.gradle.api.tasks.compile.JavaCompile
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.withType

class GenerateAvroPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        val extension = target.extensions.create("generateAvro", GenerateAvroPluginExtension::class.java)
        val task = target.tasks.register("generateAvro", GenerateAvroTask::class.java) {
            files.set(extension.files)
            output.set(target.layout.buildDirectory.dir("generated/sources/avro/main/java"))
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