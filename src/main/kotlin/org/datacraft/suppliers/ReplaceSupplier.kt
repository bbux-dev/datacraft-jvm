package org.datacraft.suppliers

import org.datacraft.ValueSupplier

class ReplaceSupplier(
    private val wrapped: ValueSupplier<*>,
    private val replacements: Map<String, ValueSupplier<*>>
) : ValueSupplier<String> {
    override fun next(iteration: Long): String {
        var value = wrapped.next(iteration).toString()
        for ((oldValue, newValueSupplier) in replacements) {
            value = value.replace(oldValue, newValueSupplier.next(iteration).toString())
        }
        return value
    }
}