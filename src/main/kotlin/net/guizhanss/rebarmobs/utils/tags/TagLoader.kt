package net.guizhanss.rebarmobs.utils.tags

import com.google.gson.Gson
import com.google.gson.JsonElement
import org.bukkit.Bukkit
import org.bukkit.Keyed
import org.bukkit.NamespacedKey
import org.bukkit.Registry
import org.bukkit.plugin.java.JavaPlugin
import java.io.File
import java.util.Locale
import java.util.jar.JarFile
import java.util.logging.Level

internal class TagLoader<T : Keyed>(
    private val plugin: JavaPlugin,
    private val registry: Registry<T>,
    private val clazz: Class<T>,
    private val tagRegistryKey: String,
    private val type: String = clazz.simpleName.toSnakeCase(),
) {
    private val gson = Gson()
    private val namespace = NamespacedKey(plugin, "tag_loader").namespace
    private val resourceRoot = "tags/$type"
    private val dataRoot = File(plugin.dataFolder, resourceRoot)
    private val loadedTags = mutableMapOf<NamespacedKey, Set<T>>()

    fun load(tags: Iterable<RebarMobsTag<T>>) {
        copyBundledTags()
        loadedTags.clear()

        tags.forEach { tag ->
            runCatching {
                loadTag(tag.getKey(), mutableSetOf())
            }.onSuccess {
                tag.values = it
            }.onFailure {
                tag.values = emptySet()
                plugin.logger.log(Level.SEVERE, it) { "An error occurred while loading tag ${tag.getKey()}" }
            }
        }
    }

    private fun copyBundledTags() {
        bundledTagResources().forEach { resourcePath ->
            val target = File(plugin.dataFolder, resourcePath)
            if (target.exists()) {
                return@forEach
            }

            target.parentFile.mkdirs()
            plugin.getResource(resourcePath)?.use { source ->
                target.outputStream().use { output ->
                    source.copyTo(output)
                }
            }
        }
    }

    private fun bundledTagResources(): List<String> {
        val source = plugin.javaClass.protectionDomain.codeSource?.location?.toURI()?.let(::File) ?: return emptyList()

        return when {
            source.isDirectory -> {
                val resourceDirectory = File(source, resourceRoot)
                if (!resourceDirectory.isDirectory) {
                    emptyList()
                } else {
                    resourceDirectory.walkTopDown().filter { it.isFile && it.extension == "json" }
                        .map { it.relativeTo(source).invariantSeparatorsPath }.toList()
                }
            }

            source.isFile -> JarFile(source).use { jar ->
                jar.entries().asSequence().map { it.name }
                    .filter { it.startsWith("$resourceRoot/") && it.endsWith(".json") }.toList()
            }

            else -> emptyList()
        }
    }

    private fun loadTag(
        tagKey: NamespacedKey,
        loadingTags: MutableSet<NamespacedKey>,
    ): Set<T> {
        loadedTags[tagKey]?.let { return it }

        if (!loadingTags.add(tagKey)) {
            error("Circular $type tag reference: ${loadingTags.joinToString(" -> ")} -> $tagKey")
        }

        val values = try {
            val localTagFile = dataTagFile(tagKey)
            if (tagKey.namespace == namespace && localTagFile.isFile) {
                loadTagFile(localTagFile, loadingTags)
            } else {
                Bukkit.getTag(tagRegistryKey, tagKey, clazz)?.values ?: error("Unknown $type tag reference: #$tagKey")
            }
        } finally {
            loadingTags.remove(tagKey)
        }

        loadedTags[tagKey] = values
        return values
    }

    private fun loadTagFile(
        file: File,
        loadingTags: MutableSet<NamespacedKey>,
    ): Set<T> = file.reader().use { reader ->
        val root = gson.fromJson(reader, JsonElement::class.java)
        val valuesElement = root?.takeIf { it.isJsonObject }?.asJsonObject?.get("values")

        require(valuesElement != null && valuesElement.isJsonArray) {
            "Tag file ${file.path} must be a JSON object with a values array"
        }

        valuesElement.asJsonArray.flatMap { parseTagValue(it, file, loadingTags) }.toSet()
    }

    private fun parseTagValue(
        valueElement: JsonElement,
        file: File,
        loadingTags: MutableSet<NamespacedKey>,
    ): Set<T> {
        require(valueElement.isJsonPrimitive && valueElement.asJsonPrimitive.isString) {
            "Tag value in ${file.path} must be a string"
        }

        val rawValue = valueElement.asString
        if (rawValue.startsWith("#")) {
            return loadTag(parseKey(rawValue.removePrefix("#"), file), loadingTags)
        }

        val valueKey = parseKey(rawValue, file)
        val value = registry.get(valueKey) ?: error("Unknown $type key in ${file.path}: $valueKey")
        return setOf(value)
    }

    private fun parseKey(
        rawKey: String,
        file: File,
    ): NamespacedKey = requireNotNull(NamespacedKey.fromString(rawKey)) {
        "Invalid namespaced key in ${file.path}: $rawKey"
    }

    private fun dataTagFile(tagKey: NamespacedKey): File = File(dataRoot, "${tagKey.key}.json")
}

private fun String.toSnakeCase(): String = replace(Regex("([a-z0-9])([A-Z])"), "$1_$2").lowercase(Locale.ENGLISH)
