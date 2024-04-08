package org.datacraft

object Specs {
    fun values(values: List<*>?): Map<String, Any?> {
        return mapOf("type" to "values", "data" to values)
    }

    fun values(weights: Map<Float?, *>?): Map<String, Any?> {
        return mapOf("type" to "values", "data" to weights)
    }
}
