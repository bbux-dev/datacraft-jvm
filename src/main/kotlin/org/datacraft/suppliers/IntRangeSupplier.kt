package org.datacraft.suppliers

import org.datacraft.ValueSupplier

internal class IntRangeSupplier(
    private val start: Int,
    private val end: Int,
    private val step: Int? = 1
) : ValueSupplier<Int> {
    private var current = start

    override fun next(iteration: Long): Int {
        val result = current
        current += step ?: 1
        if (current > end) {
            current = start
        }
        return result
    }
}