import org.gradle.api.DefaultTask
import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction
import org.openapitools.generator.gradle.plugin.tasks.GenerateTask
import javax.inject.Inject

abstract class GenerateOpenApiTask @Inject constructor(objectFactory: ObjectFactory) : DefaultTask() {

    private val original = GenerateTask(objectFactory)

    @get:InputFiles
    abstract val files: Property<Collection<String>>

    @get:Input
    abstract val packageName: Property<String>

    @get:OutputDirectory
    abstract val output: Property<String>

    @TaskAction
    fun run() {
        original.generatorName.set("spring")
        original.outputDir.set(output)
        original.configOptions.set(
            mapOf(
                "apiPackage" to packageName.get(),
                "modelPackage" to packageName.get(),
                "interfaceOnly" to "true",
                "skipDefaultInterface" to "true",
                "sourceFolder" to "",
                "annotationLibrary" to "none",
                "documentationProvider" to "none",
                "useSpringBoot3" to "true",
                "openApiNullable" to "false",
                "useTags" to "true"
            )
        )
        original.typeMappings.set(mapOf("DateTime" to "Instant"))
        original.importMappings.set(mapOf("Instant" to "java.time.Instant"))
        files.get().forEach { file ->
            original.inputSpec.set(file)
            original.doWork()
        }
    }
}