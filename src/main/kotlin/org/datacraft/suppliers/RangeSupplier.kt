package org.datacraft.suppliers

import org.datacraft.ValueSupplier

class RangeSupplier(
    private val start: Int,
    private val end: Int
) : ValueSupplier<Any> {
    private var current = start

    override fun next(iteration: Long): Any {
        val result = current
        current++
        if (current > end) {
            current = start
        }
        return result
    }
}