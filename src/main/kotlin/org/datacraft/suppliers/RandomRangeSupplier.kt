package org.datacraft.suppliers
import kotlin.random.Random
import org.datacraft.ValueSupplier

class RandomRangeSupplier(
    private val start: Double,
    private val end: Double
) : ValueSupplier<Any> {
    override fun next(iteration: Long): Any {
        return Random.nextDouble(start, end)
    }
}