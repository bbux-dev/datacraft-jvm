package org.datacraft

import com.google.gson.Gson
import kotlinx.serialization.json.Json
import java.util.*
import kotlin.NoSuchElementException

class DataSpec(val data: Map<String, FieldSpec>) {
    private var count = 0L
    private var loader = Loaders.init(this)

    fun generator(iterations: Long): Iterator<Map<String, Any?>> = object : Iterator<Map<String, Any?>> {
        override fun hasNext(): Boolean = count < iterations

        override fun next(): Map<String, Any?> {
            if (!hasNext()) throw NoSuchElementException()
            count++

            val record: MutableMap<String, Any?> = mutableMapOf();
            for (entry in data.entries) {
                val value = loader.get(entry.key).next(count)
                record[entry.key] = value
            }
            return record;
        }
    }

    companion object {
        fun parse(raw: Map<String, Any?>): DataSpec {
            return DataSpec(parseRaw(raw))
        }

        fun parseRaw(raw: Map<String, Any?>): Map<String, FieldSpec> {
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
            return null
        }

        fun parseString(json: String): DataSpec {
            val processed = Preprocessor.preprocessSpec(Json.parseToJsonElement(json));
            @Suppress("UNCHECKED_CAST")
            val data: Map<String, Any?> = Gson().fromJson(processed, Map::class.java) as Map<String, Any?>
            return parse(data)
        }
    }
}

