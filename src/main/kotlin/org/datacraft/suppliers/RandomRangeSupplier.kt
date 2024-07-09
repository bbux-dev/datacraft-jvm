package org.datacraft.suppliers
import kotlin.random.Random
import org.datacraft.ValueSupplier

class RandomRangeSupplier(
    private val start: Double,
    private val end: Double
) : ValueSupplier<Double> {
    override fun next(iteration: Long): Double {
        return Random.nextDouble(start, end)
    }
}