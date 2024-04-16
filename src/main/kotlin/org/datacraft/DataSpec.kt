package org.datacraft

import com.google.gson.Gson
import kotlinx.serialization.json.Json
import java.util.*
import kotlin.NoSuchElementException

/**
 * A class for producing data records from field specifications.
 *
 * @param data a map from field names to field specifications, which describe how to generate values for each field.
 */
class DataSpec(val data: Map<String, FieldSpec>) {
    private var count = 0L
    private var loader = Loaders.init(this)
    private val gson = Gson()

    /**
     * Generates an iterator over maps representing data records, each conforming to the specified field specifications.
     *
     * @param iterations the number of records to generate.
     * @return an iterator that produces a series of maps, where each map represents a data record.
     */
    fun generator(iterations: Long): Iterator<Map<String, Any?>> = object : Iterator<Map<String, Any?>> {
        override fun hasNext(): Boolean = count < iterations

        override fun next(): Map<String, Any?> {
            if (!hasNext()) throw NoSuchElementException()
            count++

            val record: MutableMap<String, Any?> = mutableMapOf()
            for (entry in data.entries) {
                val value = loader.get(entry.key).next(count)
                record[entry.key] = value
            }
            return record
        }
    }

    /**
     * Generates an iterator over instances of a specified type, populated with data according to the field specifications.
     *
     * This method uses Gson to serialize each map into an instance of the specified class type. This method is useful
     * when you need to produce data records as instances of a data class or other class types.
     *
     * @param iterations the number of records to generate.
     * @param type the class of the type to which the generated records should be converted.
     * @return an iterator over instances of the specified type.
     * @throws JsonSyntaxException if JSON serialization or deserialization fails.
     */
    fun <T> generateRecords(iterations: Long, type: Class<T>): Iterator<T> = object : Iterator<T> {
        private val baseIterator = generator(iterations)

        override fun hasNext(): Boolean = baseIterator.hasNext()

        override fun next(): T {
            val map = baseIterator.next()
            return gson.fromJson(gson.toJsonTree(map), type)
        }
    }

    companion object {
        /**
         * Parses a JSON string into a `DataSpec` instance.
         *
         *  @param json The JSON string to parse.
         * @return a new `DataSpec` instance based on the JSON input.
         */
        fun parseString(json: String): DataSpec {
            val processed = Preprocessor.preprocessSpec(Json.parseToJsonElement(json))
            @Suppress("UNCHECKED_CAST")
            val data: Map<String, Any?> = Gson().fromJson(processed, Map::class.java) as Map<String, Any?>
            return parse(data)
        }

        /**
         * Parses a raw map into a `DataSpec` instance. Map should adhere to DataSpec JSON standard
         *
         * @param raw The raw map input where keys are field names and values are specifications or references.
         * @return a new `DataSpec` instance based on the parsed field specifications.
         */
        fun parse(raw: Map<String, Any?>): DataSpec {
            return DataSpec(parseRaw(raw))
        }

        private fun parseRaw(raw: Map<String, Any?>): Map<String, FieldSpec> {
            val specs = mutableMapOf<String, FieldSpec>()
            raw.forEach { (key, value) ->
                if (key.equals("refs", ignoreCase = true) && value is Map<*, *>) {
                    @Suppress("UNCHECKED_CAST")
                    specs.putAll(parseRaw(value as Map<String, Any?>))
                }
                specs[key] = fieldSpecFrom(value) ?: throw SpecException("Invalid value for FieldSpec $value")
            }
            return specs
        }

        /**
         * Constructs a `FieldSpec` from a raw value.
         *
         * This method supports constructing `FieldSpec` from a variety of inputs including single values, lists, or maps
         * specifying detailed configurations.
         *
         * @param value The value to convert into a `FieldSpec`.
         * @return a `FieldSpec` corresponding to the provided value or `null` if the value cannot be converted.
         */
        private fun fieldSpecFrom(value: Any?): FieldSpec? {
            if (value is Map<*, *>) {
                @Suppress("UNCHECKED_CAST")
                val spec = value as Map<String, Any?>
                @Suppress("UNCHECKED_CAST")
                val config = spec.getOrDefault("config", Collections.emptyMap<String, Any>()) as Map<String, Any>
                return FieldSpec(
                    spec["type"] as String,
                    spec["data"],
                    config
                )
            }
            if (value is List<*>) {
                return FieldSpec(
                    "values",
                    value,
                    null
                )
            }
            // Handle cases for all primitive types and String
            when (value) {
                is Int, is Long, is Float, is Double, is Byte, is Short, is Boolean, is String -> {
                    return FieldSpec(
                        "values",
                        value,
                        null
                    )
                }
            }
            return null
        }
    }

}

