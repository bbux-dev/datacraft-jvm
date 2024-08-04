package org.datacraft.models

interface KeyProvider {
    /**
     * Get the next set of field names to process.
     *
     * @return a pair of key group name and key list for the group name
     */
    fun get(): Pair<String, List<String>>
}