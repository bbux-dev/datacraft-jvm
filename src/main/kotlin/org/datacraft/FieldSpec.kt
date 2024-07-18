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

    internal class BasicFieldSpec(type: String, data: Any?, config: Map<String, Any>? = null) :
        FieldSpec(type, data, config) {
        override fun toString(): String {
            return "FieldSpec(type=$type, data=$data, config=$config)"
        }
    }

    companion object {

        fun basic(type: String, data: Any?, config: Map<String, Any>): FieldSpec {
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
            if (type == "combine-list") {
                val refs = rawSpec["refs"]

                @Suppress("UNCHECKED_CAST")
                val refsList = refs as? List<List<String>>
                    ?: throw SpecException("refs pointer must be list of lists: i.e [[\"ONE\", \"TWO\"]]. $rawSpec")

                if (refsList.isEmpty() || refsList[0] !is List) {
                    throw SpecException("refs list must contain at least one list. $rawSpec")
                }
                return CombineListFieldSpec(config, refs)
            }
            if (type == "ref") {
                val ref = rawSpec["ref"] ?: rawSpec["data"]
                ?: throw SpecException("one of ref or data must be defined for ref specs: $rawSpec")
                return RefFieldSpec(config, ref.toString())
            }
            if (type == "ref_list") {
                val refs = rawSpec["refs"] ?: rawSpec["data"]
                ?: throw SpecException("one of refs or data must be defined for ref_list specs: $rawSpec")
                val refList: List<String> = when (refs) {
                    is String -> listOf(refs)
                    is List<*> -> if (refs.all { it is String }) refs.filterIsInstance<String>() else throw SpecException(
                        "refs or data must be a list of strings: $rawSpec"
                    )

                    else -> throw SpecException("refs or data must be a string or a list of strings: $rawSpec")
                }
                return RefListFieldSpec(config, refList)
            }
            if (type == "replace") {
                val ref =
                    rawSpec["ref"] ?: throw SpecException("ref and data must be defined for replace specs: $rawSpec")
                val data =
                    rawSpec["data"] ?: throw SpecException("ref and data must be defined for replace specs: $rawSpec")
                return ReplaceFieldSpec(config, ref.toString(), data)
            }
            return basic(type, rawSpec["data"], config)
        }

        private fun parseListField(key: String, rawSpec: Map<String, Any?>): List<String> {
            if (!rawSpec.containsKey(key)) {
                return listOf()
            }
            return when (val field = rawSpec[key]) {
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

internal class CombineFieldSpec(
    config: Map<String, Any>?, val refs: List<String>?, val fields: List<String>? = null
) :
    FieldSpec("combine", null, config)

internal class CombineListFieldSpec(
    config: Map<String, Any>?, val refs: List<List<String>>
) :
    FieldSpec("combine-list", null, config)


internal class RefFieldSpec(config: Map<String, Any>?, val ref: String) :
    FieldSpec("ref", null, config)


internal class RefListFieldSpec(config: Map<String, Any>?, val refs: List<String>) :
    FieldSpec("ref_list", null, config)

internal class ReplaceFieldSpec(config: Map<String, Any>?, val ref: String, data: Any?) :
    FieldSpec("ref", data, config)