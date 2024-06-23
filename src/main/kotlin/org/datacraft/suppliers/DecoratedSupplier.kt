package org.datacraft.suppliers

import org.datacraft.ValueSupplier

class DecoratedSupplier(
    private val supplier: ValueSupplier<out Any?>,
    config: Map<String, Any>
) : ValueSupplier<Any> {

    private val prefix: String = config["prefix"] as? String ?: ""
    private val suffix: String = config["suffix"] as? String ?: ""

    override fun next(iteration: Long): Any {
        val value = supplier.next(iteration)
        return "${prefix}${value}${suffix}"
    }
}
