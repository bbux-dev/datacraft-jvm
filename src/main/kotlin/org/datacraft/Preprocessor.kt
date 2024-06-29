package org.datacraft

import kotlinx.serialization.json.*
import java.net.URLDecoder

internal object Preprocessor {
    /**
     *
     */
    fun preprocessSpec(rawSpec: JsonElement, pretty: Boolean = false): String {
        // Check if the input is a JsonObject
        if (rawSpec !is JsonObject)
            throw SpecException(
                "Cannot process non object specs! ${Json.encodeToString(JsonElement.serializer(), rawSpec)}"
            )

        val updatedSpec = buildJsonObject {
            // Iterate through each entry in the JsonObject
            rawSpec.forEach { (key, spec) ->
                when {
                    key == "refs" -> {
                        // Call a function to preprocess the 'refs' and add to updatedSpecs
                        put(key, Json.parseToJsonElement(preprocessSpec(spec)))
                    }

                    key == "field_groups" -> {
                        // Directly add the 'field_groups' to updatedSpecs
                        put(key, spec)
                    }

                    key.contains("?") -> {
                        // Handle keys with parameters
                        updateWithParams(key, spec, this)
                    }

                    else -> {
                        // Handle keys without parameters
                        updateNoParams(key, spec, this)
                    }
                }
            }
        }
        val encoder = Json { prettyPrint = pretty }
        return encoder.encodeToString(JsonElement.serializer(), updatedSpec)
    }

    /**
     * Updates the specification based on parameters included in the key.
     *
     * @param key The input key potentially containing parameters.
     * @param spec The JSON element (specification) to be updated.
     * @param updatedSpecs The JSON object that accumulates all updated specifications.
     */
    private fun updateWithParams(key: String, spec: JsonElement, updatedSpecs: JsonObjectBuilder) {
        val (newKey, specType, params) = parseKey(key)
        val transformedSpec = convertToValuesIfNeeded(spec, specType)

        val originalConfig = transformedSpec.jsonObject["config"]?.jsonObject

        // Create a new config JsonObject by first copying all existing values from originalConfig
        var config = buildJsonObject {
            originalConfig?.forEach { (key, value) ->
                put(key, value)  // Copy existing config values
            }
            // Now add or overwrite with new params
            params.forEach { (paramKey, paramValue) ->
                put(paramKey, JsonPrimitive(paramValue))
            }
        }
        config = buildJsonObject {
            // Copy all elements from the original 'config' to the new one, except 'cnt'
            config.forEach { (key, value) ->
                if (key != "cnt") {
                    put(key, value)
                } else {
                    // If the key is 'cnt', rename it to 'count'
                    put("count", value)
                }
            }
        }

        val updated = buildJsonObject {
            transformedSpec.jsonObject.forEach { (key, value) ->
                put(key, value)
            }
            // if defined, update it
            specType?.let { put("type", JsonPrimitive(it)) }
            put("config", config)
        }

        updatedSpecs.put(newKey, updated)
    }

    /**
     * Converts the provided JSON spec to a "values" spec format if it is identified as data only.
     *
     * @param spec The JSON element to potentially convert.
     * @param specType Optional type information that might impact the determination of whether the spec is data only.
     * @return A JsonElement, either the original or a new JsonObject with "type" and "data" fields if conversion was necessary.
     */
    private fun convertToValuesIfNeeded(spec: JsonElement, specType: String?): JsonElement {
        return if (isSpecData(spec, specType)) {
            buildJsonObject {
                put("type", "values")
                put("data", spec)
            }
        } else {
            spec
        }
    }

    /**
     * Parses the key expected to have URL format. There are two main forms:
     * 1. field:field_type?param1=val&param2=val...
     * 2. field?param1=val...
     *
     * @param fieldName The field name in URL-like format.
     * @return Triple containing the new key, optional type, and a map of parameters.
     */
    fun parseKey(fieldName: String): Triple<String, String?, Map<String, String>> {
        val parts = fieldName.split("?", limit = 2)
        val keyType = parts[0].split(":", limit = 2)
        val parsedQuery = if (parts.size > 1) parseQuery(parts[1]) else emptyMap()

        val newKey = if (keyType.size > 1) keyType[0] else parts[0]
        val specType = keyType.getOrNull(1)

        return Triple(newKey, specType, parsedQuery)
    }

    /**
     * Parses the query part of a URL-like string.
     *
     * @param query The query string part after '?'.
     * @return A map representing the query parameters.
     */
    private fun parseQuery(query: String): Map<String, String> {
        return query.split("&")
            .mapNotNull {
                it.split("=", limit = 2).takeIf { pair -> pair.size == 2 }
            }
            .associate { (key, value) ->
                key.trim() to URLDecoder.decode(value, "UTF-8")
            }
    }


    /**
     * Updates the JSON object with no parameters in the key.
     *
     * @param key The JSON key which may include a type annotation.
     * @param spec The JSON element associated with the key.
     * @param jsonObjectBuilder A builder for constructing the resultant JSON object.
     */
    private fun updateNoParams(key: String, spec: JsonElement, jsonObjectBuilder: JsonObjectBuilder) {
        var (newKey, specType) = parseKey(key)
        if (specType == null) {
            specType = extractType(spec)
        }
        val transformedSpec = if (isSpecData(spec, specType)) {
            buildJsonObject {
                put("type", specType ?: "values")
                put("data", spec)
            }
        } else {
            // Handle the case where spec is a JsonObject but needs an updated type
            (spec as JsonObject).let {
                buildJsonObject {
                    // Copy all existing fields
                    it.forEach { (key, value) ->
                        put(key, value)
                    }
                    // Overwrite or set the "type" field
                    put("type", JsonPrimitive(specType))
                }
            }
        }

        jsonObjectBuilder.put(newKey, transformedSpec)
    }


    /**
     * Determines if the provided JSON specification represents raw data rather than a structured specification.
     *
     * This function checks whether the JSON element (spec) contains only data or represents a structured specification
     * based on certain core fields. It also considers the 'spectype' which might indicate that the spec is inherently
     * not just data (e.g., 'nested').
     *
     * @param spec The JSON element to check.
     * @param spectype An optional string indicating a specific type associated with the spec, which might influence the determination.
     * @return True if the spec is considered to be only data, False if it is considered a structured specification.
     */
    private fun isSpecData(spec: JsonElement, spectype: String?): Boolean {
        // Check for a 'nested' type, either in 'spectype' or as a 'type' field within the spec.
        if (spectype == "nested" || extractType(spec) == "nested") {
            return false
        }
        // If spec is not a JsonObject, then it's raw data.
        if (spec !is JsonObject) {
            return true
        }
        // Define core fields that indicate structured specifications.
        val coreFields = setOf("type", "data", "config", "ref", "refs", "fields")
        // Check for presence of any core fields in the JsonObject.
        if (coreFields.any { it in spec }) {
            return false
        }
        // Consider empty JsonObjects as not just data, possibly abbreviating a more complex specification.
        if (spec.isEmpty()) {
            return false
        }
        // Spec does not contain core fields and is not empty; treat as data.
        return true
    }

    /**
     * Extracts the type field from a JSON object if it exists.
     *
     * @param spec The JSON element from which to extract the type.
     * @return The content of the "type" field if the element is a JsonObject and the field exists, otherwise null.
     */
    private fun extractType(spec: JsonElement): String? {
        return if (spec is JsonObject && spec["type"]?.jsonPrimitive != null) {
            spec["type"]?.jsonPrimitive?.content
        } else {
            null
        }
    }

}