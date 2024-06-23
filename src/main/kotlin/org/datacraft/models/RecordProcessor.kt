package org.datacraft.models

/**
 * Interface representing a processor that processes records into strings.
 */
interface RecordProcessor {
    /**
     * Processes the given record into the appropriate output string.
     *
     * @param record the generated record for the current iteration
     * @return the formatted record
     */
    fun process(record: Map<String, Any>): String

    /**
     * Processes the given list of records into the appropriate output string.
     *
     * @param records the generated records for the current iteration
     * @return the formatted records
     */
    fun process(records: List<Map<String, Any>>): String
}