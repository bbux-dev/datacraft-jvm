package org.example.org.datacraft

import org.datacraft.FieldSpec

class DataSpec(private val data: Map<String, FieldSpec>) {
    private var count = 0L

    fun generator(iterations: Long): Iterator<Map<String, Any?>> = object : Iterator<Map<String, Any?>> {
        override fun hasNext(): Boolean = count < iterations

        override fun next(): Map<String, Any?> {
            if (!hasNext()) throw NoSuchElementException()
            count++
            // Implement the logic to generate the next data map
            // For now, returning an empty map as a placeholder
            return emptyMap()
        }
    }
}

