import org.apache.avro.tool.SpecificCompilerTool
import org.gradle.api.DefaultTask
import org.gradle.api.file.Directory
import org.gradle.api.logging.LogLevel
import org.gradle.api.provider.Property
import org.gradle.api.provider.Provider
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction

abstract class GenerateAvroTask : DefaultTask() {

    @get:InputFiles
    abstract val files: Property<Collection<String>>

    @get:OutputDirectory
    abstract val output: Property<Provider<Directory>>

    @TaskAction
    fun run() {
        logging.captureStandardOutput(LogLevel.INFO);
        logging.captureStandardError(LogLevel.ERROR)
        val arguments = mutableListOf<String>()
        arguments.addAll(
            listOf(
                "-encoding", "UTF-8",
                "-string",
                "-fieldVisibility", "private",
                "-noSetters",
                "schema"
            )
        )
        arguments.addAll(files.get())
        arguments.add(output.get().get().toString())
        SpecificCompilerTool().run(System.`in`, System.out, System.err, arguments)
    }
}