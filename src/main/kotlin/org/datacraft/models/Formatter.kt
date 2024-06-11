package org.datacraft.models

/**
 * Interface for formatting a record represented as a Map into a String.
 *
 * Implementations of this interface can be used to format records in different formats
 * such as JSON, CSV, YAML, etc. The format method will be called with a record, and
 * the implementation should return the formatted string representation of the record.
 *
 * Example implementation:
 * ```
 * class SimpleFormatter : Formatter {
 *     override name() -> "simple"
 *     override fun format(record: Map<String, Any?>): String {
 *         return record.toString()
 *     }
 * }
 * ```
 */
interface Formatter {
    /**
     * Name of this formatter
     */
    fun name(): String
    /**
     * Formats the given record as a String.
     *
     * @param record a map representing the record to be formatted
     * @return the formatted string representation of the record
     */
    fun format(record: Map<String, Any?>): String
    /**
     * Formats the given record as a String.
     *
     * @param records a list of maps representing the records to be formatted
     * @return the formatted string representation of the record
     */
    fun format(records: List<Map<String, Any?>>): String
}