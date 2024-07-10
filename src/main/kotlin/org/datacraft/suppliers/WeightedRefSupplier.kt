package org.datacraft.suppliers

import org.datacraft.ValueSupplier

internal class WeightedRefSupplier(private val keySupplier: ValueSupplier<Any>, private val valuesMap: Map<String, ValueSupplier<Any>>) :
    ValueSupplier<Any> {
    override fun next(iteration: Long): Any {
        val key = keySupplier.next(iteration)
        return  valuesMap[key]!!.next(iteration)
    }
}