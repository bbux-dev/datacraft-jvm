package org.datacraft

class DataSpec(val data: Map<String, FieldSpec>) {
    private var count = 0L

    fun generator(iterations: Long): Iterator<Map<String, Any?>> = object : Iterator<Map<String, Any?>> {
        override fun hasNext(): Boolean = count < iterations

        override fun next(): Map<String, Any?> {
            if (!hasNext()) throw NoSuchElementException()
            count++

            val record: Map<String, Any> = mutableMapOf();
            for (entry in data.entries) {

            }
            return record;
        }
    }
}

