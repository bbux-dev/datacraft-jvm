package org.datacraft.format

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import org.datacraft.models.Formatter

/**
 * A JSON formatter that formats a record represented as a Map into a JSON string.
 *
 * This formatter can be registered as an SPI implementation to handle JSON formatting.
 */
class JsonFormatterPretty : Formatter {
    private val gson: Gson = GsonBuilder().setPrettyPrinting().create()
    override fun name(): String = "json-pretty"

    /**
     * Formats the given record as a JSON string.
     *
     * @param record a map representing the record to be formatted
     * @return the pretty print formatted JSON string representation of the record
     */
    override fun format(record: Map<String, Any?>): String {
        return gson.toJson(record)
    }

    /**
     * Formats the given records as a JSON string.
     *
     * @param records a list of maps representing the record to be formatted
     * @return the formatted JSON string representation of the records
     */
    override fun format(records: List<Map<String, Any?>>): String {
        return gson.toJson(records)
    }
}
