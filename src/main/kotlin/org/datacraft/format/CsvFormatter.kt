package org.datacraft.format

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import org.datacraft.models.Formatter


/**
 * A CSV formatter that formats records represented as a Map into CSV strings.
 *
 * This formatter can be registered as an SPI implementation to handle CSV formatting.
 */
class CsvFormatter : Formatter {
    /**
     * Returns the name of this formatter.
     *
     * @return the name of the formatter
     */
    override fun name(): String = "csv"

    /**
     * Formats a single record as a CSV string.
     *
     * @param record a map representing the record to be formatted
     * @return the formatted CSV string representation of the record
     */
    override fun format(record: Map<String, Any?>): String {
        return record.values.joinToString(",") { it.toString() }
    }

    /**
     * Formats a list of records as a CSV string with headers.
     *
     * @param records a list of maps, each representing a record to be formatted
     * @return the formatted CSV string representation of the records with headers
     */
    override fun format(records: List<Map<String, Any?>>): String {
        if (records.isEmpty()) return ""

        val lines = records.map { format(it) }

        return listOf(lines).joinToString("\n")
    }
}
