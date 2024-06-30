package org.datacraft

import java.util.*

/**
 * Represents the specification for a field, including its type, data, and configuration.
 *
 * @property type The type of the field, which dictates how the data should be interpreted or generated.
 * @property data The data associated with the field, whose format and type can vary depending on the field type.
 * @property config A map of configuration options that can modify how the data is processed or generated.
 */
sealed class FieldSpec(val type: String, val data: Any?, val config: Map<String, Any>?) {

    internal class BasicFieldSpec(type: String, data: Any?, config: Map<String, Any>? = null) : FieldSpec(type, data, config)
    companion object {

        private fun basic(type: String, data: Any?, config: Map<String, Any>): FieldSpec {
            return BasicFieldSpec(type, data, config)
        }
        /**
         * Creates a [FieldSpec] instance for a 'values' type field with the given data and an empty configuration map.
         *
         * @param data The data for the 'values' field, can be of any object type.
         * @return A new [FieldSpec] instance configured for 'values' type.
         */
        fun values(data: Any): FieldSpec {
            return BasicFieldSpec("values", data, Collections.emptyMap())
        }

        /**
         * Overloaded method to create a [FieldSpec] instance for a 'values' type field specifically with a list of data.
         *
         * @param data The list of data for the 'values' field.
         * @return A new [FieldSpec] instance configured for 'values' type with list data.
         */
        fun values(data: List<Any?>): FieldSpec {
            return BasicFieldSpec("values", data, Collections.emptyMap())
        }

        /**
         * Overloaded method to create a [FieldSpec] instance for a 'values' type field specifically with a map of data.
         *
         * @param data The map of data for the 'values' field, typically used for key-value pairs.
         * @return A new [FieldSpec] instance configured for 'values' type with map data.
         */
        fun values(data: Map<String?, Float?>): FieldSpec {
            return BasicFieldSpec("values", data, Collections.emptyMap())
        }

        fun forType(type: String, rawSpec: Map<String, Any?>): FieldSpec {
            @Suppress("UNCHECKED_CAST")
            val config = rawSpec.getOrDefault("config", Collections.emptyMap<String, Any>()) as Map<String, Any>
            if (type == "combine") {
                val refs = parseListField("refs", rawSpec)
                val fields = parseListField("fields", rawSpec)
                if (refs.isEmpty() && fields.isEmpty()) {
                    throw SpecException("Need to specify one of fields or refs for combine spec")
                }
                return CombineFieldSpec(config, refs, fields)
            }
            return basic(type, rawSpec["data"], config)
        }

        private fun parseListField(key: String, rawSpec: Map<String, Any?>): List<String> {
            if (!rawSpec.containsKey(key)) {
                return listOf()
            }
            return when(val field = rawSpec[key]) {
                is String -> listOf(field)
                is List<*> -> {
                    if (field.all { it is String }) {
                        @Suppress("UNCHECKED_CAST")
                        field as List<String>
                    } else {
                        throw SpecException("All $key values must be strings")
                    }
                }
                null -> listOf()
                else -> throw SpecException("$key Field must be a string or a list of strings")
            }
        }
    }
}

internal class CombineFieldSpec(config: Map<String, Any>?, val refs : List<String>, val fields: List<String> ) :
    FieldSpec("combine", null, config)
