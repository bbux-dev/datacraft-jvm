package org.datacraft.suppliers

import org.datacraft.ValueSupplier
import kotlin.random.Random

class SampleSupplier(private val data : List<Any>) : ValueSupplier<Any> {
    override fun next(iteration: Long): Any {
        val idx = Random.nextInt(0, data.size)
        return data[idx]
    }
}