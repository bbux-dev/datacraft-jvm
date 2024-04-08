package org.datacraft

import java.util.Collections

/**
 * Represents the specification for a field, including its type, data, and configuration.
 *
 * @property type The type of the field, which dictates how the data should be interpreted or generated.
 * @property data The data associated with the field, whose format and type can vary depending on the field type.
 * @property config A map of configuration options that can modify how the data is processed or generated.
 */
class FieldSpec(val type: String, val data: Any, val config: Map<String, Any>) {

    companion object {
        /**
         * Creates a [FieldSpec] instance with the specified type and data, using an empty configuration map.
         *
         * @param type The type of the field.
         * @param data The data for the field.
         * @return A new [FieldSpec] instance.
         */
        fun make(type: String, data: Any): FieldSpec {
            return FieldSpec(type, data, Collections.emptyMap())
        }

        /**
         * Creates a [FieldSpec] instance for a 'values' type field with the given data and an empty configuration map.
         *
         * @param data The data for the 'values' field, can be of any object type.
         * @return A new [FieldSpec] instance configured for 'values' type.
         */
        fun values(data: Any): FieldSpec {
            return FieldSpec("values", data, Collections.emptyMap())
        }

        /**
         * Overloaded method to create a [FieldSpec] instance for a 'values' type field specifically with a list of data.
         *
         * @param data The list of data for the 'values' field.
         * @return A new [FieldSpec] instance configured for 'values' type with list data.
         */
        fun values(data: List<Any?>): FieldSpec {
            return FieldSpec("values", data, Collections.emptyMap())
        }

        /**
         * Overloaded method to create a [FieldSpec] instance for a 'values' type field specifically with a map of data.
         *
         * @param data The map of data for the 'values' field, typically used for key-value pairs.
         * @return A new [FieldSpec] instance configured for 'values' type with map data.
         */
        fun values(data: Map<String?, Float?>): FieldSpec {
            return FieldSpec("values", data, Collections.emptyMap())
        }
    }
}
