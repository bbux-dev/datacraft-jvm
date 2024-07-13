package org.datacraft.loaders

import org.datacraft.*
import org.datacraft.models.ValueSupplierLoader

class CharClassLoader : ValueSupplierLoader<String> {
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

        // Escape characters if 'escape' key exists in config
        if (config.containsKey("escape")) {
            val escape = config["escape"] as String
            val escapeStr = config.getOrDefault("escape_str", "\\") as String
            modifiedData = escapeCharacters(modifiedData, escape, escapeStr)
        }

        return Suppliers.characterClass(
            modifiedData,
            mean = gitConfigAsDouble(config, "mean"),
            stddev = gitConfigAsDouble(config, "stddev"),
            count = gitConfigAsInt(config, "count"),
            min = gitConfigAsInt(config, "min"),
            max = gitConfigAsInt(config, "max")
        )
    }

    private fun excludeCharacters(data: String, exclude: String): String {
        return data.filterNot { it in exclude }
    }

    private fun escapeCharacters(data: String, escape: String, escapeStr: String): String {
        val escapedData = StringBuilder()
        data.forEach { char ->
            if (char in escape) {
                escapedData.append(escapeStr).append(char)
            } else {
                escapedData.append(char)
            }
        }
        return escapedData.toString()
    }
}