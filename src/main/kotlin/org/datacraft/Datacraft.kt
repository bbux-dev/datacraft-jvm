package org.datacraft

import com.google.gson.Gson
import kotlinx.serialization.json.Json
import java.util.Collections

object Datacraft {
    fun parse(raw: Map<String, Any?>): Map<String, FieldSpec> {
        val specs = mutableMapOf<String, FieldSpec>()
        raw.forEach { (key, value) ->
            if (key.equals("refs", ignoreCase = true) && value is Map<*, *>) {
                @Suppress("UNCHECKED_CAST")
                specs.putAll(parse(value as Map<String, Any?>))
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
                spec["data"] ?: throw IllegalArgumentException("Data must not be null"),
                config
            )
        }
        return null
    }

    fun parseString(json: String): DataSpec {
        val processed = Preprocessor.preprocessSpec(Json.parseToJsonElement(json));
        @Suppress("UNCHECKED_CAST")
        val data: Map<String, Any?> = Gson().fromJson(processed, Map::class.java) as Map<String, Any?>
        return DataSpec(parse(data))
    }
}
