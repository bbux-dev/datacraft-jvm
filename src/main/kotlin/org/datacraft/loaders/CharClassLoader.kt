package org.datacraft.loaders

import org.datacraft.*
import org.datacraft.models.ValueSupplierLoader

open class CharClassLoader : ValueSupplierLoader<String> {
    override fun typeNames(): List<String> = listOf("char_class")

    override fun load(spec: FieldSpec, loader: Loader): ValueSupplier<String> {
        if (spec.data == null) {
            throw SpecException("Data is a required field for char_class type: $spec")
        }

        var data = spec.data
        val config = (spec.config ?: mapOf()).toMutableMap()

        if (data is String && CharClassMappings.CLASS_MAPPING.containsKey(data)) {
            data = CharClassMappings.CLASS_MAPPING[data]!!
        }

        if (data is List<*>) {
            val newData = data.map { datum ->
                if (datum is String && CharClassMappings.CLASS_MAPPING.containsKey(datum)) {
                    CharClassMappings.CLASS_MAPPING[datum]!!
                } else {
                    datum.toString()
                }
            }
            data = newData.joinToString("")
        }

        if (!config.containsKey("join_with")) {
            config["join_with"] = Registries.getDefault<String>("char_class_join_with")
        }

        var modifiedData = data as String

        // Exclude characters if 'exclude' key exists in config
        if (config.containsKey("exclude")) {
            val exclude = config["exclude"] as String
            modifiedData = excludeCharacters(modifiedData, exclude)
        }
        val supplier = Suppliers.characterClass(
            modifiedData,
            mean = gitConfigAsDouble(config, "mean"),
            stddev = gitConfigAsDouble(config, "stddev"),
            count = gitConfigAsInt(config, "count"),
            min = gitConfigAsInt(config, "min"),
            max = gitConfigAsInt(config, "max")
        )

        // Escape characters if 'escape' key exists in config
        if (config.containsKey("escape")) {
            val escape = config["escape"] as String
            val escapeStr = config.getOrDefault("escape_str", "\\") as String
            val replacements: Map<String, String> = escape.associate { it.toString() to "$escapeStr$it" }
            return Suppliers.replace(supplier, replacements)
        }
        return supplier
    }

    private fun excludeCharacters(data: String, exclude: String): String {
        return data.filterNot { it in exclude }
    }
}