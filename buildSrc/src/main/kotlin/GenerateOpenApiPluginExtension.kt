import org.gradle.api.provider.Property

interface GenerateOpenApiPluginExtension {

    val files: Property<Collection<String>>
    val packageName: Property<String>
}