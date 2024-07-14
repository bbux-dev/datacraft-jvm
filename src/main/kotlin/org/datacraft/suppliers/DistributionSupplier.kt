package org.datacraft.suppliers

import org.datacraft.ValueSupplier
import org.datacraft.models.Distribution

class DistributionSupplier(private val dist: Distribution) : ValueSupplier<Double> {
    override fun next(iteration: Long): Double {
        return dist.nextValue()
    }
}