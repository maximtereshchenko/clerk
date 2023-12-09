import org.gradle.api.provider.Property

interface GenerateAvroPluginExtension {

    val files: Property<Collection<String>>
}